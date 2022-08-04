package com.example.demo.src.MainPage;

import com.example.demo.src.MainPage.model.GetNewFootprintsRes;
import com.example.demo.src.MainPage.model.GetTrendingFootprintsRes;
import com.example.demo.src.User.UserController;
import com.example.demo.src.User.model.GetProfileRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MainPageDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    // Trending this week
    // 만약 하위 페이지를 가지지 않는 블럭이 트렌딩에 속하면? - 해결 완 select 에 if문
    public List<GetTrendingFootprintsRes> getTrendingFootsteps(){
        String getTrendingFootprintsQuery = "select b.userId, sap.blockId, if(b.childPageId is null, null, (select p.preview from Block b, Page p where b.childPageId = p.pageId)) as preview, count(*) as footprintNum\n" +
                "from StampAndPrint sap, Block b\n" +
                "where sap.blockId = b.blockId\n" +
                "    and sap.status=1\n" +
                "    and sap.stampOrPrint = 'P'\n" +
                "    and sap.updatedAt BETWEEN DATE_ADD(NOW(),INTERVAL -1 WEEK ) AND NOW()\n" +
                "group by blockId\n" +
                "order by count(*) desc\n" +
                "limit 12;";

        String getFootprintNumQuery = "select count(*)\n" +
                "from StampAndPrint sap\n" +
                "where sap.status=1\n" +
                "  and sap.stampOrPrint = 'P'\n" +
                "  and sap.blockId=?;";

        return this.jdbcTemplate.query(getTrendingFootprintsQuery,
                (rs, rowNum) -> new GetTrendingFootprintsRes(
                        rs.getInt("userId"),
                        rs.getInt("blockId"),
                        rs.getString("preview"),
                        jdbcTemplate.queryForObject(getFootprintNumQuery,
                                int.class
                                , rs.getInt("blockId"))
                ));
    }


}


