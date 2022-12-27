package ru.ecosystem.dreamjob.app.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.ecosystem.dreamjob.app.model.WorkingMode;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkingModeRowMapper implements RowMapper<WorkingMode> {

    @Override
    public WorkingMode mapRow(ResultSet rs, int rowNum) throws SQLException {
        return WorkingMode.builder()
                .id(rs.getLong("id"))
                .mode(WorkingMode.Type.valueOf(rs.getString("mode")))
                .build();
    }
}
