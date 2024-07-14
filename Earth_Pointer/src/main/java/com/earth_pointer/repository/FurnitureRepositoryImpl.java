package com.earth_pointer.repository;

import com.earth_pointer.domain.Furniture;
import com.earth_pointer.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Repository
public class FurnitureRepositoryImpl {

    private final DataSource dataSource;

    @Autowired
    public FurnitureRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addFurniture(String name, String description, int price) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "insert into furniture (furniture_id, name, description, price) values (furnitureSeq.nextVal, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setInt(3, price);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }

    }

    public ArrayList<Furniture> printFurniture() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Furniture> furnitureList = new ArrayList<>();

        try {
            conn = dataSource.getConnection();

            String sql = "select * from furniture";
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                Furniture furniture = new Furniture(
                        rs.getInt("furniture_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("price")
                );
                furnitureList.add(furniture);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseUtils.close(conn, ps, rs);
        }

        return furnitureList;
    }

}
