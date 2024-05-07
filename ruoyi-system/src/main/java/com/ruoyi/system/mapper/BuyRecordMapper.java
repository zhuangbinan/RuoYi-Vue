package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.system.domain.BuyRecordDTO;
import com.ruoyi.system.domain.LastBallsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * BuyRecordMapper
 *
 * @author ruoyi
 */
@Mapper
public interface BuyRecordMapper
{

    public Page<BuyRecordDTO> query10Chance(Page page, @Param("userName")String userName, @Param("countNum")String countNum);

    /**
     * 查询还没兑奖的
     * @param userName
     * @param countNum
     * @return
     */
    public List<BuyRecordDTO> queryNoCheckChance(@Param("userName")String userName, @Param("countNum")String countNum);

    //按照用户购买的记录来作为开奖依据
    List<BuyRecordDTO> queryAllUserAndCountNum();

    void insertOne(BuyRecordDTO buyRecordDTO);

    int updateById(BuyRecordDTO buyRecordDTO);
}
