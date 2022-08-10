package com.example.demo.src.Follow;


import com.example.demo.src.Follow.model.DeleteFollowRes;
import com.example.demo.src.Follow.model.GetFollowInfoRes;
import com.example.demo.src.Follow.model.PostFollowReq;
import com.example.demo.src.Follow.model.PostFollowRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class FollowDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetFollowInfoRes getFollow(int userId){
        // followee 개수
        String getFolloweeNumByUserId = "select count(*)\n" +
                "from Follow\n" +
                "where follower = ?";
        // follower 개수
        String getFollowerNumByUserId = "select count(*)\n" +
                "from Follow\n" +
                "where followee = ?";

        int followeeNum = this.jdbcTemplate.queryForObject(getFolloweeNumByUserId,int.class,userId);
        int followerNum = this.jdbcTemplate.queryForObject(getFollowerNumByUserId,int.class,userId);
        return new GetFollowInfoRes(followeeNum,followerNum);
    }

    public PostFollowRes createFollow(PostFollowReq postFollowReq){
        String createFollowInfoQuery = "insert into Follow (followee, follower) values (?,?);";
        String getFollowInfoQuery ="select follower,followee,status,createdAt\n" +
                "from Follow\n" +
                "where follower =? and followee =?";

        Object[] createFollowParams = new Object[]{postFollowReq.getUserId(),postFollowReq.getFollowedId()};
        Object[] getFollowParams = new Object[]{postFollowReq.getClass(),postFollowReq.getFollowedId()};
        this.jdbcTemplate.update(createFollowInfoQuery,createFollowParams);

        return this.jdbcTemplate.queryForObject(getFollowInfoQuery,
                (rs,num)-> new PostFollowRes(
                        rs.getTimestamp("createdAt"),
                        rs.getInt("status"),
                        rs.getInt("followee"),
                        rs.getInt("follower")
                )
                ,getFollowParams);
    }
    public DeleteFollowRes deleteFollow(PostFollowReq postFollowReq){
        String deleteFollowInfoQuery = "insert into Follow (followee, follower) values (?,?);";
        Object[] createFollowParams = new Object[]{postFollowReq.getUserId(),postFollowReq.getFollowedId()};

        this.jdbcTemplate.update(deleteFollowInfoQuery,createFollowParams);

        return new DeleteFollowRes(postFollowReq.getUserId(),postFollowReq.getFollowedId(),"삭제되었습니다.");
    }

}
