package cn.mbtt.service.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class JsonMapTypeHandler implements TypeHandler<Map<String, Object>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setParameter(PreparedStatement ps, int i, Map<String, Object> parameter, JdbcType jdbcType) throws SQLException {
        try {
            String json = objectMapper.writeValueAsString(parameter);
            ps.setString(i, json);
        } catch (IOException e) {
            throw new SQLException("Error converting Map to JSON", e);
        }
    }

    @Override
    public Map<String, Object> getResult(ResultSet rs, String columnName) throws SQLException {
        try {
            String json = rs.getString(columnName);
            if (json != null) {
                return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            }
            return null;
        } catch (IOException e) {
            throw new SQLException("Error converting JSON to Map", e);
        }
    }

    @Override
    public Map<String, Object> getResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            String json = rs.getString(columnIndex);
            if (json != null) {
                return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            }
            return null;
        } catch (IOException e) {
            throw new SQLException("Error converting JSON to Map", e);
        }
    }

    @Override
    public Map<String, Object> getResult(java.sql.CallableStatement cs, int columnIndex) throws SQLException {
        try {
            String json = cs.getString(columnIndex);
            if (json != null) {
                return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            }
            return null;
        } catch (IOException e) {
            throw new SQLException("Error converting JSON to Map", e);
        }
    }
}
