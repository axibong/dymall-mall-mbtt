package cn.mbtt.service.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JsonListTypeHandler implements TypeHandler<List<String>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        try {
            String json = objectMapper.writeValueAsString(parameter);
            ps.setString(i, json);
        } catch (IOException e) {
            throw new SQLException("Error converting List to JSON", e);
        }
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        try {
            String json = rs.getString(columnName);
            if (json != null) {
                return objectMapper.readValue(json, new TypeReference<List<String>>() {});
            }
            return null;
        } catch (IOException e) {
            throw new SQLException("Error converting JSON to List", e);
        }
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            String json = rs.getString(columnIndex);
            if (json != null) {
                return objectMapper.readValue(json, new TypeReference<List<String>>() {});
            }
            return null;
        } catch (IOException e) {
            throw new SQLException("Error converting JSON to List", e);
        }
    }

    @Override
    public List<String> getResult(java.sql.CallableStatement cs, int columnIndex) throws SQLException {
        try {
            String json = cs.getString(columnIndex);
            if (json != null) {
                return objectMapper.readValue(json, new TypeReference<List<String>>() {});
            }
            return null;
        } catch (IOException e) {
            throw new SQLException("Error converting JSON to List", e);
        }
    }
}
