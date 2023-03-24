package com.example.superherov5.repositories;

import com.example.superherov5.dto.City;
import com.example.superherov5.dto.PowerCount;
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
                superheroes.add(new Superhero(heroName, realName, creationYear));
            }
            return superheroes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Superhero getSuperhero(String name) {
        Superhero superheroObj = null;
        try {
            SQL = "SELECT * FROM superhero WHERE heroname = ?";
            ps = connect().prepareStatement(SQL);
            ps.setString(1, name);
            rs = ps.executeQuery();
            if(rs.next()) {
                String heroName = rs.getString("heroname");
                String realName = rs.getString("realname");
                int creationYear = rs.getInt("creationyear");
                superheroObj = new Superhero(heroName, realName, creationYear);
            }
            return superheroObj;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SuperPower> getHeroAndPowers() {
        List<SuperPower> superheroes = new ArrayList<>();
        try {
            SQL = "SELECT heroname, powername FROM superhero " +
                    "JOIN superheropower ON superhero.id = superheropower.superheroid " +
                    "JOIN superpower ON superheropower.superpowerid  = superpower.id";
            ps = connect().prepareStatement(SQL);
            rs = ps.executeQuery();

            while(rs.next()) {
                SuperPower currentPower = new SuperPower();

                String heroName = rs.getString("heroname");
                String powerName = rs.getString("powername");

                List<String> powers = new ArrayList<>();
                currentPower.setPowers(powers);
                currentPower.addSuperPower(powerName);
                currentPower.setHeroName(heroName);

                superheroes.add(currentPower);
            }
            return superheroes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public SuperPower getPowersForOne(String heroName) {
        SuperPower superPower = null;
        try {
            SQL = "SELECT heroname, powername FROM superhero " +
                    "JOIN superheropower ON superhero.id = superheropower.superheroid " +
                    "JOIN superpower ON superheropower.superpowerid  = superpower.id " +
                    "AND heroname = ?";
            ps = connect().prepareStatement(SQL);
            ps.setString(1, heroName);
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
            SQL = "SELECT * FROM city";
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
            SQL = "SELECT * FROM superpower";
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

    public void updateHero(SuperHeroForm form, String heroName) {
        try {
            int heroId = 0;
            SQL = "SELECT ID FROM superhero WHERE heroname = ?";
            ps = connect().prepareStatement(SQL);
            ps.setString(1, heroName);
            rs = ps.executeQuery();
            if(rs.next()) {
                heroId = rs.getInt("id");
            }
            SQL = "UPDATE superhero SET heroname = ?. realname = ?, creationyear = ?, cityid = ? WHERE id = ?";
            ps = connect().prepareStatement(SQL);
            ps.setInt(1, form.getHeroId());
            ps.setString(2, form.getHeroName());
            ps.setString(3, form.getRealName());
            ps.setInt(4, form.getCreationYear());
            ps.setString(5, form.getCity());
            ps.executeUpdate();



        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteHero(String heroName) {
        try {
            int heroId = 0;
            SQL = "SELECT id FROM superhero WHERE heroname = ?";
            ps = connect().prepareStatement(SQL);
            ps.setString(1, heroName);
            rs = ps.executeQuery();
            if (rs.next()) {
                heroId = rs.getInt("id");
            }

            SQL = "DELETE FROM superheropower WHERE superheroid = ?";
            ps = connect().prepareStatement(SQL);
            ps.setInt(1, heroId);
            ps.executeUpdate();

            SQL = "DELETE FROM superhero WHERE heroname = ?";
            ps = connect().prepareStatement(SQL);
            ps.setString(1, heroName);
            ps.executeUpdate();

        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
