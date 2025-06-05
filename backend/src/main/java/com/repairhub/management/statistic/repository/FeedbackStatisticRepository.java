package com.repairhub.management.statistic.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.repair.enums.FaultType;
import com.repairhub.management.repair.enums.FeedbackType;
import com.repairhub.management.statistic.dto.NegativeFeedbackStatDTO;

@Repository
public class FeedbackStatisticRepository {
    private final NamedParameterJdbcTemplate jdbc;

    public FeedbackStatisticRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    
    private final RowMapper<NegativeFeedbackStatDTO> mapper = new RowMapper<>() {
        @Override
        public NegativeFeedbackStatDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            String techNamesConcat = rs.getString("tech_names");
            List<String> techList = techNamesConcat == null || techNamesConcat.isBlank()
                    ? List.of()
                    : Arrays.stream(techNamesConcat.split(","))
                            .map(String::trim)
                            .collect(Collectors.toList());

            return NegativeFeedbackStatDTO.builder()
                .feedbackId(rs.getLong("feedback_id"))
                .orderId(rs.getLong("order_id"))
                .feedbackType(FeedbackType.valueOf(rs.getString("feed_back_type")))
                .rating(rs.getObject("rating") != null ? rs.getInt("rating") : null)
                .customerName(rs.getString("customer_name"))
                .faultType(FaultType.valueOf(rs.getString("fault_type")))
                .repairmanName(techList)
                .orderStatus(OrderStatus.valueOf(rs.getString("order_status")))
                .feedbackContent(rs.getString("description"))
                .feedbackTime(rs.getTimestamp("feedback_time").toLocalDateTime())
                .build();
        }
    };

    public List<NegativeFeedbackStatDTO> findAllNegativeFeedbacks() {
        String sql = """
            SELECT
              fb.feedback_id,
              fb.order_id,
              fb.feed_back_type,
              fb.rating,
              cu.username        AS customer_name,
              ro.fault_type,
              GROUP_CONCAT(DISTINCT rm.username) AS tech_names,
              ro.status          AS order_status,
              fb.description,
              fb.feedback_time
            FROM feedback fb
            JOIN repair_order ro ON fb.order_id = ro.order_id
            JOIN users cu ON fb.user_id = cu.user_id
            LEFT JOIN assignment asg 
              ON asg.order_id = ro.order_id
            LEFT JOIN users rm 
              ON asg.repairman_id = rm.user_id
            WHERE (fb.feed_back_type = 'URGENT' OR fb.rating <= 3)
            GROUP BY
              fb.feedback_id,
              fb.order_id,
              fb.feed_back_type,
              fb.rating,
              cu.username,
              ro.fault_type,
              ro.status,
              fb.description,
              fb.feedback_time
            ORDER BY fb.feedback_time DESC
            """;
        return jdbc.query(sql, Collections.emptyMap(), mapper);
    }


    public List<NegativeFeedbackStatDTO> findNegativeFeedbacksSince(LocalDateTime since) {
        String sql = """
            SELECT
              fb.feedback_id,
              fb.order_id,
              fb.feed_back_type,
              fb.rating,
              cu.username        AS customer_name,
              ro.fault_type,
              GROUP_CONCAT(DISTINCT rm.username) AS tech_names,
              ro.status          AS order_status,
              fb.description,
              fb.feedback_time
            FROM feedback fb
            JOIN repair_order ro ON fb.order_id = ro.order_id
            JOIN users cu ON fb.user_id = cu.user_id
            LEFT JOIN assignment asg 
              ON asg.order_id = ro.order_id
            LEFT JOIN users rm 
              ON asg.repairman_id = rm.user_id
            WHERE (fb.feed_back_type = 'URGENT' OR fb.rating <= 3)
              AND fb.feedback_time >= :since
            GROUP BY
              fb.feedback_id,
              fb.order_id,
              fb.feed_back_type,
              fb.rating,
              cu.username,
              ro.fault_type,
              ro.status,
              fb.description,
              fb.feedback_time
            ORDER BY fb.feedback_time DESC
            """;

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("since", since);

        return jdbc.query(sql, params, mapper);
    }
}
