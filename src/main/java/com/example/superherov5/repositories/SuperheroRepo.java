package com.example.superherov5.repositories;

import com.example.superherov5.dto.SuperHeroForm;
import com.example.superherov5.dto.SuperPower;
import com.example.superherov5.model.Superhero;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("superhero_DB")
public class SuperheroRepo implements ISuperheroRepo{
    private String SQL;
    private Statement stmt;
    private ResultSet rs;
    private PreparedStatement ps;

    @Value("${spring.datasource.url}")
    private String db_url;

    @Value("${spring.datasource.username}")
    private String uid;

    @Value("${spring.datasource.password}")
    private String pwd;

    public Connection connect() {
        Connection con;
        try {
            con = DriverManager.getConnection(db_url, uid, pwd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }

    public List<Superhero> getSuperheroes() {
        List<Superhero> superheroes = new ArrayList<>();
        try {
            SQL = "SELECT * FROM superhero ORDER BY creationyear ASC";
            stmt = connect().createStatement();
            rs = stmt.executeQuery(SQL);
            while(rs.next()) {
                int id = rs.getInt("id");
                String heroName = rs.getString("heroname");
                String realName = rs.getString("realname");
                int creationYear = rs.getInt("creationyear");
                superheroes.add(new Superhero(id, heroName, realName, creationYear));
            }
            return superheroes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public SuperHeroForm findSuperHeroById(int id) {
        SuperHeroForm superheroForm = null;
        try {
            String SQL = "SELECT superhero.id, superhero.heroname, superhero.realname, superhero.creationyear, city.cityname " +
                    "FROM superhero " +
                    "INNER JOIN city ON superhero.cityid = city.id WHERE superhero.id = ?";
            PreparedStatement ps = connect().prepareStatement(SQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int heroId = rs.getInt("id");
                String heroName = rs.getString("heroname");
                String realName = rs.getString("realname");
                int creationYear = rs.getInt("creationyear");
                String city = rs.getString("cityname");
                List<String> powerList = getSuperheroPowers(id);
                superheroForm = new SuperHeroForm(heroId, heroName, realName, creationYear, city, powerList);
            }
            rs.close();
            ps.close();
            connect().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return superheroForm;
    }

    public List<String> getSuperheroPowers(int superheroId) {
        List<String> powerList = new ArrayList<>();
        try {
            String SQL = "SELECT superpower.powername FROM superheropower " +
                    "INNER JOIN superpower ON superheropower.superpowerid = superpower.id WHERE superheropower.superheroid = ?";
            PreparedStatement ps = connect().prepareStatement(SQL);
            ps.setInt(1, superheroId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String power = rs.getString("powername");
                powerList.add(power);
            }
            rs.close();
            ps.close();
            connect().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return powerList;
    }


    public SuperPower getPowersForOne(int heroId) {
        SuperPower superPower = null;
        try {
            SQL = "SELECT superhero.heroname, superpower.powername " +
                    "FROM superhero " +
                    "INNER JOIN superheropower ON superhero.id = superheropower.superheroid " +
                    "INNER JOIN superpower ON superheropower.superpowerid = superpower.id " +
                    "WHERE superhero.id = ?";

            ps = connect().prepareStatement(SQL);
            ps.setInt(1, heroId);
            rs = ps.executeQuery();
            String heroname = "";
            List<String> results = new ArrayList<>();
             while(rs.next()) {
                heroname = rs.getString("heroname");
                results.add(rs.getString("powername"));
            }
            return new SuperPower(heroname, results);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getCities() {
        List<String> cities = new ArrayList<>();
        try {
            SQL = "SELECT * FROM city ORDER BY cityname ASC";
            stmt = connect().createStatement();
            rs = stmt.executeQuery(SQL);
            while(rs.next()) {
                String cityName = rs.getString("cityname");
                cities.add(cityName);
            }
            return cities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getSuperPowers() {
        List<String> superPower = new ArrayList<>();
        try {
            SQL = "SELECT powername FROM superpower ORDER BY powername ASC";
            stmt = connect().createStatement();
            rs = stmt.executeQuery(SQL);
            while(rs.next()) {
                String power = rs.getString("powername");
                superPower.add(power);
            }
            return superPower;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //CREATE Superhero
    public void addSuperhero(SuperHeroForm form) {
        try {
            // ID's
            int cityId = 0;
            int heroId = 0;
            List<Integer> powerIDs = new ArrayList<>();

            // find city_id
            String SQL1 = "select id from city where cityname = ?";
            PreparedStatement pstmt = connect().prepareStatement(SQL1);
            pstmt.setString(1, form.getCity());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                cityId = rs.getInt("id");
            }

            // insert row in superhero table
            String SQL2 = "insert into superhero (heroname, realname, creationyear, cityid) " +
                    "values(?, ?, ?, ?)";
            pstmt = connect().prepareStatement(SQL2, Statement.RETURN_GENERATED_KEYS); // return autoincremented key
            pstmt.setString(1, form.getHeroName());
            pstmt.setString(2, form.getRealName());
            pstmt.setInt(3, form.getCreationYear());
            pstmt.setInt(4, cityId);
            int rows = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                heroId = rs.getInt(1);
            }


            // find power_ids
            String SQL3 = "select id from superpower where powername = ?;";
            pstmt = connect().prepareStatement(SQL3);

            for (String power : form.getPowerList()) {
                pstmt.setString(1, power);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    powerIDs.add(rs.getInt("id"));
                }
            }

            // insert entries in superhero_powers join table
            String SQL4 = "insert into superheropower values (?,?);";
            pstmt = connect().prepareStatement(SQL4);

            for (int i = 0; i < powerIDs.size(); i++) {
                pstmt.setInt(1, heroId);
                pstmt.setInt(2, powerIDs.get(i));
                rows = pstmt.executeUpdate();
            }

        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //UPDATE Superhero
    public void updateHero(int id, SuperHeroForm form) {
        try {
            String cityQuery = "SELECT id FROM city WHERE cityname = ?";
            PreparedStatement cityPs = connect().prepareStatement(cityQuery);
            cityPs.setString(1, form.getCity());
            ResultSet cityRs = cityPs.executeQuery();
            int cityId = 0;
            if (cityRs.next()) {
                cityId = cityRs.getInt("id");
            }
            cityRs.close();
            cityPs.close();

            // Then, update the superhero using the fetched cityid
            String SQL = "UPDATE superhero SET heroname = ?, realname = ?, creationyear = ?, cityid = ? WHERE id = ?";
            PreparedStatement ps = connect().prepareStatement(SQL);
            ps.setString(1, form.getHeroName());
            ps.setString(2, form.getRealName());
            ps.setInt(3, form.getCreationYear());
            ps.setInt(4, cityId);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

        deleteSuperheroPowers(id);
        addSuperheroPowers(id, form.getPowerList());
    }

    public void deleteSuperheroPowers(int heroId) {
        try {
            SQL = "DELETE FROM superheropower WHERE superheroid = ?";
            ps = connect().prepareStatement(SQL);
            ps.setInt(1, heroId);
            ps.executeUpdate();
            ps.close();
            connect().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void addSuperheroPowers(int heroId, List<String> powers) {
        PreparedStatement localPs;
        try {
            String SQL = "INSERT INTO superheropower (superheroid, superpowerid) VALUES (?, ?)";
            localPs = connect().prepareStatement(SQL);

            for (String powerName : powers) {
                int powerId = getPowerId(powerName);
                localPs.setInt(1, heroId);
                localPs.setInt(2, powerId);
                localPs.addBatch();
            }

            localPs.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPowerId(String powerName) {
        int powerId = 0;
        String SQL = "SELECT id FROM superpower WHERE powername = ?";
        try (Connection connection = connect();
             PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, powerName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    powerId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return powerId;
    }

    //DELETE Superhero
    public void deleteHero(int heroId) {
        try {
            SQL = "SELECT id FROM superhero WHERE id = ?";
            ps = connect().prepareStatement(SQL);
            ps.setInt(1, heroId);
            rs = ps.executeQuery();
            if (rs.next()) {
                heroId = rs.getInt("id");
            }

            SQL = "DELETE FROM superheropower WHERE superheroid = ?";
            ps = connect().prepareStatement(SQL);
            ps.setInt(1, heroId);
            ps.executeUpdate();

            SQL = "DELETE FROM superhero WHERE id = ?";
            ps = connect().prepareStatement(SQL);
            ps.setInt(1, heroId);
            ps.executeUpdate();

        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
