package ru.ecosystem.dreamjob.app.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.ecosystem.dreamjob.app.model.Candidate;
import ru.ecosystem.dreamjob.app.model.WorkingMode;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CandidateRowMapper implements RowMapper<Candidate> {

    @Override
    public Candidate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Candidate.builder()
                .id(rs.getLong("candidate_id"))
                .created(rs.getTimestamp("candidate_created").toLocalDateTime())
                .name(rs.getString("candidate_name"))
                .price(rs.getString("candidates_price"))
                .description(rs.getString("candidates_description"))
                .photo(rs.getBytes("candidate_photo"))
                .workingMode(WorkingMode.builder()
                        .mode(WorkingMode.Type.valueOf(rs.getString("working_modes_mode")))
                        .id(rs.getLong("working_modes_id"))
                        .build())
                .build();
    }
}
