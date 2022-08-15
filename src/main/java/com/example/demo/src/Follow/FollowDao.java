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
                "where followee = ?";
        // follower 개수
        String getFollowerNumByUserId = "select count(*)\n" +
                "from Follow\n" +
                "where follower = ?";

        int followeeNum = this.jdbcTemplate.queryForObject(getFolloweeNumByUserId,int.class,userId);
        int followerNum = this.jdbcTemplate.queryForObject(getFollowerNumByUserId,int.class,userId);
        return new GetFollowInfoRes(followeeNum,followerNum);
    }

    public PostFollowRes createFollow(FollowReq createFollowReq){
        String createFollowInfoQuery = "insert into Follow (follower, followee) values (?,?)";
        String getFollowInfoQuery ="select follower,followee,status,createdAt\n" +
                "from Follow\n" +
                "where follower =? and followee =?";
        Object[] createFollowParams = new Object[]{createFollowReq.getUserId(),createFollowReq.getFollowedId()};
        Object[] getFollowParams = new Object[]{createFollowReq.getUserId(),createFollowReq.getFollowedId()};
        this.jdbcTemplate.update(createFollowInfoQuery,createFollowParams);

        return this.jdbcTemplate.queryForObject(getFollowInfoQuery,
                (rs,num)-> new PostFollowRes(
                        rs.getTimestamp("createdAt"),
                        rs.getInt("status"),
                        rs.getInt("follower"),
                        rs.getInt("followee")
                )
                ,getFollowParams);
    }
    public DeleteFollowRes deleteFollow(FollowReq deletedFollowReq){ // 팔로우 상태 수정해주기
        String deleteFollowInfoQuery = "delete from Follow\n" +
                "where follower = ? and followee = ?";
        Object[] deleteFollowParams = new Object[]{deletedFollowReq.getUserId(),deletedFollowReq.getFollowedId()};

        this.jdbcTemplate.update(deleteFollowInfoQuery,deleteFollowParams);

        return new DeleteFollowRes(deletedFollowReq.getUserId(),deletedFollowReq.getFollowedId());
    }

//    public PostFollowRes modifyFollow(FollowReq modifyFollowReq){
//        String modifyFollowQuery = "update Follow set status = 1\n" +
//                " where Follow.followee = ? and Follow.follower = ?;";
//        String modifyFollowResQuery = "select updatedAt,status,follower,followee\n" +
//                "from Follow\n" +
//                "where follower = ? and followee = ? ";
//        Object[] modifyFollowParam = new Object[]{modifyFollowReq.getUserId(),modifyFollowReq.getFollowedId()};
//        this.jdbcTemplate.update(modifyFollowQuery,modifyFollowParam);
//        return this.jdbcTemplate.queryForObject(modifyFollowResQuery,
//                (rs,num)-> new PostFollowRes(
//                        rs.getTimestamp("updatedAt"),
//                        rs.getInt("status"),
//                        rs.getInt("follower"),
//                        rs.getInt("followee")
//                )
//                ,modifyFollowParam);
//    }

//    public boolean checkExist(FollowReq followReq){
//        Object[] followReqParams = new Object[]{followReq.getUserId(),followReq.getFollowedId()};
//        String checkExistQuery  ="select exists(\n" +
//                "    select *\n" +
//                "    from Follow\n" +
//                "    where follower = ? and followee = ?\n" +
//                "           ) as isChk";
//        return this.jdbcTemplate.queryForObject(checkExistQuery,boolean.class,followReqParams);
//    }
}
