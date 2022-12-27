package ru.ecosystem.dreamjob.app.mapper;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PairRowMapper implements RowMapper<Pair<Boolean, Long>> {

    @Override
    public Pair<Boolean, Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Pair.of(Boolean.TRUE, rs.getLong("id"));
    }
}
