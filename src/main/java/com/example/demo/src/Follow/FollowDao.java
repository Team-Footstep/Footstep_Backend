package com.example.demo.src.Follow;


import com.example.demo.src.Follow.model.*;
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

    public PostFollowRes createFollow(FollowReq createFollowReq){
        String createFollowInfoQuery = "insert into Follow (followee, follower) values (?,?);";
        String getFollowInfoQuery ="select follower,followee,status,createdAt\n" +
                "from Follow\n" +
                "where follower =? and followee =?";

        Object[] createFollowParams = new Object[]{createFollowReq.getUserId(),createFollowReq.getFollowedId()};
        Object[] getFollowParams = new Object[]{createFollowReq.getClass(),createFollowReq.getFollowedId()};
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
    public DeleteFollowRes deleteFollow(FollowReq deletedFollowReq){
        String deleteFollowInfoQuery = "insert into Follow (followee, follower) values (?,?);";
        Object[] createFollowParams = new Object[]{deletedFollowReq.getUserId(),deletedFollowReq.getFollowedId()};

        this.jdbcTemplate.update(deleteFollowInfoQuery,createFollowParams);

        return new DeleteFollowRes(deletedFollowReq.getUserId(),deletedFollowReq.getFollowedId(),"삭제되었습니다.");
    }

    public PostFollowRes modifyFollow(FollowReq modifyFollowReq){
        String modifyFollowQuery = "";
        String modifyFollowResQuery = "";
        Object[] modifyFollowParam = new Object[]{modifyFollowReq.getUserId(),modifyFollowReq.getFollowedId()};
        this.jdbcTemplate.update(modifyFollowQuery,modifyFollowParam);
        return this.jdbcTemplate.queryForObject(modifyFollowResQuery,
                (rs,num)-> new PostFollowRes(
                        rs.getTimestamp("updatedAt"),
                        rs.getInt("status"),
                        rs.getInt("follower"),
                        rs.getInt("followee")
                )
                ,modifyFollowParam);
    }

    public boolean checkExist(FollowReq followReq){
        String checkExistQuery  ="select exists(\n" +
                "    select *\n" +
                "    from Follow\n" +
                "    where followee = ? and follower = ?\n" +
                "           ) as isChk";
        return this.jdbcTemplate.queryForObject(checkExistQuery,boolean.class);
    }
}
