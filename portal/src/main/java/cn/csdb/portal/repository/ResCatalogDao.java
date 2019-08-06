package cn.csdb.portal.repository;

import cn.csdb.portal.model.ResCatalog_Mongo;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by shibaoping on 2018/10/22.
 */
@Repository
public class ResCatalogDao {


    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private JdbcTemplate jdbcTemplate;

    public int insertLocalResCatalog(final ResCatalog_Mongo resCatalog) {
        /*final String sql = "insert into t_localcatalog(parentid,name,level,nodeorder,updatetime,createtime) VALUES (?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int i = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setInt(1, resCatalog.getParentid());
                ps.setString(2, resCatalog.getName());
                ps.setInt(3, resCatalog.getLevel());
                ps.setInt(4, resCatalog.getNodeorder());
                ps.setTimestamp(5, new Timestamp(resCatalog.getUpdatetime().getTime()));
                ps.setTimestamp(6, new Timestamp(resCatalog.getCreatetime().getTime()));
                return ps;
            }
        }, keyHolder);
        if (i > 0) {
            return keyHolder.getKey().intValue();
        }
        return -1;*/

        Query query = new Query();
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"rid")));
        ResCatalog_Mongo res = this.mongoTemplate.find(query, ResCatalog_Mongo.class).get(0);
        resCatalog.setParentid(resCatalog.getParentid());
        resCatalog.setName(resCatalog.getName());
        resCatalog.setLevel(resCatalog.getLevel());
        resCatalog.setNodeorder(resCatalog.getNodeorder());
        resCatalog.setUpdatetime(resCatalog.getUpdatetime());
        resCatalog.setCreatetime(resCatalog.getCreatetime());
        resCatalog.setRid(resCatalog.getRid());
        mongoTemplate.save(resCatalog);
        return 1;
    }

    public int updateLocalResCatalog(ResCatalog_Mongo resCatalog) {
        /*String sql = "update t_localcatalog set name=?,parentid=?,level=?,nodeorder=?,updatetime=? where id=?";
        return jdbcTemplate.update(sql, resCatalog.getName(), resCatalog.getParentid(), resCatalog.getLevel(), resCatalog.getNodeorder(), resCatalog.getUpdatetime(), resCatalog.getId());*/

        mongoTemplate.findAndModify(new Query(Criteria.where("rid").is(resCatalog.getRid())),
                new Update().set("name", resCatalog.getName()).set("parentid", resCatalog.getParentid()).set("level", resCatalog.getLevel()).
                        set("nodeOrder",  resCatalog.getNodeorder()).set("updatetime",  resCatalog.getUpdatetime()).set("createtime",  resCatalog.getCreatetime()),
                ResCatalog_Mongo.class);
        return 1;

    }

    public List<ResCatalog_Mongo> getLocalResCatalogAllRoot() {
        /*final String sql = "select * from t_localcatalog where parentid=0";
        return jdbcTemplate.query(sql, new ResCatalogMapper());*/
        return mongoTemplate.find(new Query(Criteria.where("parentid").is(0)),ResCatalog_Mongo.class);
    }

    public List<ResCatalog_Mongo> getLocalResCatalogAll() {
        /*String sql = "select * from t_localcatalog  order by updateTime ";
        return jdbcTemplate.query(sql, new ResCatalogMapper());*/

        return mongoTemplate.find(new Query(),ResCatalog_Mongo.class);
    }


    public ResCatalog_Mongo getLocalRootNode() {
       /* final String sql = "select * from t_localcatalog where level=1";
        return jdbcTemplate.query(sql, new ResCatalogMapper()).get(0);*/
        return mongoTemplate.find(new Query(Criteria.where("level").is(1)),ResCatalog_Mongo.class).get(0);

    }


    public ResCatalog_Mongo getLocalResCatalogNodeById(String resCatalogId) {
        ResCatalog_Mongo resCatalog_mongos = mongoTemplate.findById(resCatalogId,ResCatalog_Mongo.class);
        return resCatalog_mongos;

    }

    public ResCatalog_Mongo selectResCatalogNodeByRid(String rid) {
        ResCatalog_Mongo resCatalog_mongos = mongoTemplate.findOne(new Query(Criteria.where("rid").is(Integer.parseInt(rid))), ResCatalog_Mongo.class);
        return resCatalog_mongos;

    }


    public List<ResCatalog_Mongo> getLocalResCatalogChildrens(int resCatalogId) {
        /*String sql = "select * from t_localcatalog where parentid=?";
        return jdbcTemplate.query(sql, new Object[]{resCatalogId}, new ResCatalogMapper());*/
        return mongoTemplate.find(new Query(Criteria.where("parentid").is(resCatalogId)),ResCatalog_Mongo.class);
    }


    public int deleteLocalResCatalog(int id) {
        /*String sql = "delete from t_localcatalog where id=?";
        return jdbcTemplate.update(sql, id);*/
        ResCatalog_Mongo r = mongoTemplate.find(new Query(Criteria.where("rid").is(id)),ResCatalog_Mongo.class).get(0);
        mongoTemplate.remove(r);
        return 1;
    }

    public List<ResCatalog_Mongo> getLast(Date updateTime) {
       /* String sql = "select * from t_localcatalog  WHERE updateTime > ? order by updateTime ";
        return jdbcTemplate.query(sql, new Object[]{updateTime}, new ResCatalogMapper());*/
        return mongoTemplate.find(new Query(Criteria.where("updateTime").gt(updateTime)),ResCatalog_Mongo.class);
    }

}
