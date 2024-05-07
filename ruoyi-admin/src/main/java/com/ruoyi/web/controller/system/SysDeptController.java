package com.ruoyi.web.controller.system;

import java.time.LocalDateTime;
import java.util.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.BuyRecordDTO;
import com.ruoyi.system.domain.LastBallsDTO;
import com.ruoyi.system.domain.UserScoreDTO;
import com.ruoyi.system.mapper.BuyRecordMapper;
import com.ruoyi.system.mapper.LastBallsMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysDeptService;

/**
 * 部门信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController
{
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private LastBallsMapper lastBallsMapper;

    @Autowired
    private BuyRecordMapper buyRecordMapper;

    @GetMapping("/lastBalls")
    public AjaxResult lastBalls() {
        final LoginUser loginUser = SecurityUtils.getLoginUser();
        final Long userId = loginUser.getUserId();
        logger.warn("userId:" + userId);
        final List<LastBallsDTO> lastBallsDTOS = lastBallsMapper.lastBalls(loginUser.getUsername());
        Page<LastBallsDTO> page = new Page<LastBallsDTO>(1,20);
//        final IPage<LastBallsDTO> lastBallsDTOS2 = lastBallsMapper.lastBalls2(page, loginUser.getUsername());
        System.out.println(lastBallsDTOS);
//        System.out.println(lastBallsDTOS2);

//        return success().put("data", lastBallsDTOS.size() > 0 ? lastBallsDTOS.get(0) : new LastBallsDTO());
        return success().put("data", lastBallsDTOS);
    }

    @GetMapping("/getRightBallByCountNum")
    public AjaxResult getRightBallByCountNum(String countNum) {
        System.out.println("countNum:"+countNum);
        final LoginUser loginUser = SecurityUtils.getLoginUser();
        final List<LastBallsDTO> lastBallsDTOS = lastBallsMapper.getRightBallByCountNum(loginUser.getUsername(),countNum);
        return success().put("data", lastBallsDTOS);
    }

    /**
     * 按期数查询购买记录，购买是否中奖，是否兑奖
     * @param countNum
     * @param page
     * @return
     */
    @GetMapping("/getBuyRecordList")
    public AjaxResult getBuyRecordList(String countNum, Page page) {

        final String login = SecurityUtils.getUsername();
        final Page<BuyRecordDTO> dtoList = buyRecordMapper.query10Chance(page, login, countNum);
        if (dtoList.getTotal() > 0) {
            final List<BuyRecordDTO> records = dtoList.getRecords();
            System.out.println("getBuyRecordList");
            System.out.println(records);
            return success().put("data", records);
        }
        return null;
    }


    @GetMapping("/queryScore")
    public AjaxResult queryScore() {
        //查询总积分和个人积分
        final String username = SecurityUtils.getUsername();
        List<UserScoreDTO> userScoreDTOList = lastBallsMapper.queryScore(username);
        System.out.println(userScoreDTOList);
        return AjaxResult.success(userScoreDTOList);
    }


    /**
     * 按期数兑奖
     * @param countNum 期数
     * @return
     */
    @GetMapping("/checkRightBalls")
    public AjaxResult checkRightBalls(String countNum) {
        final String username = SecurityUtils.getUsername();
        if (username.equals("admin")) {
            return AjaxResult.error("管理员不支持该功能!按期数兑奖");
        }
        //查询这一期的中奖
        //a.查中奖号码
        final List<LastBallsDTO> lastBallsDTOS = lastBallsMapper.getRightBallByCountNum(username,countNum);
        if (lastBallsDTOS.size() == 0) {
            return AjaxResult.error("兑奖失败，请管理员先开奖");
        }
        //b.查购买记录
        final List<BuyRecordDTO> queryNoCheckChance = buyRecordMapper.queryNoCheckChance(username, countNum);
        final List<BuyRecordDTO> records = queryNoCheckChance;
        for (BuyRecordDTO buyRecordDTO : records) {
            final LastBallsDTO rightBallsDTO = lastBallsDTOS.get(0);

            final String[] buyRecordBlueBallsSplit = buyRecordDTO.getBlueBalls().split(",");
            final String[] rightBlueBallsSplit = rightBallsDTO.getBlueBalls().split(",");

            for (int i = 0; i < buyRecordBlueBallsSplit.length; i++) {
                buyRecordBlueBallsSplit[i] = buyRecordBlueBallsSplit[i].trim();
            }

            final List<String> buyRecordBlueBallsList = Arrays.asList(buyRecordBlueBallsSplit);
            final List<String> rightBlueBallsList = Arrays.asList(rightBlueBallsSplit);

            //判断三等奖，四等奖，五等奖，六等奖要素
            int countThird = 0;
            for (String rBlueBall : rightBlueBallsList) {
                if (buyRecordBlueBallsList.contains(rBlueBall)) {
                    countThird ++;
                }
            }

            if (buyRecordDTO.getBlueBalls().equals(rightBallsDTO.getBlueBalls()) && buyRecordDTO.getRedBall().equals(rightBallsDTO.getRedBall())) {
                buyRecordDTO.setRightBallFlag("一等奖");//中一等奖，蓝球红球都全对
                buyRecordDTO.setCheckScoreFlag("已兑奖");
                // 获得5000000积分
                //a.自己积分+5000000
                //b.总积分-5000000
                final int totalI = lastBallsMapper.updateTotalScore(-5000000);
                if (totalI != 1) {
                    return AjaxResult.error("兑一等奖出错,#-5000000");
                }
                final int userI = lastBallsMapper.updateUserScoreByUsername(5000000, SecurityUtils.getUsername());
                if (userI != 1) {
                    return AjaxResult.error("兑一等奖出错,#+5000000");
                }

            } else if (buyRecordDTO.getBlueBalls().equals(rightBallsDTO.getBlueBalls()) && !(buyRecordDTO.getRedBall().equals(rightBallsDTO.getRedBall()))) {
                buyRecordDTO.setRightBallFlag("二等奖");//中二等奖,蓝球全对，红球不对
                buyRecordDTO.setCheckScoreFlag("已兑奖");
                // 获得1000000积分
                final int totalI = lastBallsMapper.updateTotalScore(-1000000);
                if (totalI != 1) {
                    return AjaxResult.error("兑二等奖出错,#-1000000");
                }
                final int userI = lastBallsMapper.updateUserScoreByUsername(1000000, SecurityUtils.getUsername());
                if (userI != 1) {
                    return AjaxResult.error("兑二等奖出错,#+1000000");
                }

            } else if (countThird == 5 && buyRecordDTO.getRedBall().equals(rightBallsDTO.getRedBall())) {
                buyRecordDTO.setRightBallFlag("三等奖");//中三等奖，蓝球对5个，红球对1个
                buyRecordDTO.setCheckScoreFlag("已兑奖");
                // 获得3000积分
                final int totalI = lastBallsMapper.updateTotalScore(-3000);
                if (totalI != 1) {
                    return AjaxResult.error("兑三等奖出错,#-3000");
                }
                final int userI = lastBallsMapper.updateUserScoreByUsername(3000, SecurityUtils.getUsername());
                if (userI != 1) {
                    return AjaxResult.error("兑三等奖出错,#+3000");
                }
            } else if ((countThird == 4 && buyRecordDTO.getRedBall().equals(rightBallsDTO.getRedBall())) || (countThird == 5)) {
                buyRecordDTO.setRightBallFlag("四等奖");//中四等奖，(蓝球对4个，红球对1个)或 (蓝球对5个)
                buyRecordDTO.setCheckScoreFlag("已兑奖");
                // 获得200积分
                final int totalI = lastBallsMapper.updateTotalScore(-200);
                if (totalI != 1) {
                    return AjaxResult.error("兑四等奖出错,#-200");
                }
                final int userI = lastBallsMapper.updateUserScoreByUsername(200, SecurityUtils.getUsername());
                if (userI != 1) {
                    return AjaxResult.error("兑四等奖出错,#+200");
                }
            } else if (countThird == 4 || (countThird == 3 && buyRecordDTO.getRedBall().equals(rightBallsDTO.getRedBall()))) {
                buyRecordDTO.setRightBallFlag("五等奖");//中五等奖，(蓝球对4个)或 (蓝球对3个,红球对1个)
                buyRecordDTO.setCheckScoreFlag("已兑奖");
                // 获得10积分
                final int totalI = lastBallsMapper.updateTotalScore(-10);
                if (totalI != 1) {
                    return AjaxResult.error("兑五等奖出错,#-10");
                }
                final int userI = lastBallsMapper.updateUserScoreByUsername(10, SecurityUtils.getUsername());
                if (userI != 1) {
                    return AjaxResult.error("兑五等奖出错,#+10");
                }
            } else if ((countThird == 2 && buyRecordDTO.getRedBall().equals(rightBallsDTO.getRedBall()))
                    || (countThird == 1 && buyRecordDTO.getRedBall().equals(rightBallsDTO.getRedBall()))
                    || (buyRecordDTO.getRedBall().equals(rightBallsDTO.getRedBall()))) {
                buyRecordDTO.setRightBallFlag("六等奖");//中六等奖，(蓝球对2个，红球对1个) 或 (蓝球对1个，红球对1个) 或 (红球对1个)
                buyRecordDTO.setCheckScoreFlag("已兑奖");
                // 获得5积分
                final int totalI = lastBallsMapper.updateTotalScore(-5);
                if (totalI != 1) {
                    return AjaxResult.error("兑六等奖出错,#-5");
                }
                final int userI = lastBallsMapper.updateUserScoreByUsername(5, SecurityUtils.getUsername());
                if (userI != 1) {
                    return AjaxResult.error("兑六等奖出错,#+5");
                }
            }else {
                buyRecordDTO.setRightBallFlag("未中奖");
                buyRecordDTO.setCheckScoreFlag("未中奖");
                //积分不变
            }
        }

        //修改数据库数据
        int resInt = 0;
        for (BuyRecordDTO record : records) {
            resInt += buyRecordMapper.updateById(record);
            System.out.println("checkRightBalls修改数量："+resInt);
        }

        return AjaxResult.success(resInt);
    }

    //管理员按钮，给所有拥有购买记录但"待兑奖"的用户开奖
    @PreAuthorize("@ss.hasRole('admin')")
    @PostMapping("/makeRightBalls")
    public AjaxResult makeRightBalls() {
        //查询所有用户和期数
        String login = "";
        int countNum = 1;

        List<BuyRecordDTO> allUserAndCountNum = buyRecordMapper.queryAllUserAndCountNum();
        if (allUserAndCountNum == null || allUserAndCountNum.size() == 0) {
            return AjaxResult.error("用户信息和期数信息获取失败");
        }
        for (BuyRecordDTO oneUserAndCountNum : allUserAndCountNum) {
            login = oneUserAndCountNum.getUserName();
            countNum = oneUserAndCountNum.getCountNum();

            //33个蓝球
            int blueTemp = 1;
            Integer[] blueBalls = new Integer[33];
            for (int i = 0; i < blueBalls.length; i++) {
                blueBalls[i] = blueTemp;
                blueTemp++;
            }
            //16个红球
            int redTemp = 1;
            Integer[] redBalls = new Integer[16];
            for (int i = 0; i < redBalls.length; i++) {
                redBalls[i] = redTemp;
                redTemp++;
            }

            System.out.println("蓝球：" + Arrays.toString(blueBalls));
            System.out.println("红球：" + Arrays.toString(redBalls));
            System.out.println("====生成球完毕===\n现在开始摇奖，蓝球里选6个,红球里选1个");

            Random rd = new Random();
            List<Integer> rdList = new ArrayList<>();
            while (true) {
                int rdNum = rd.nextInt(33);
                if (rdList.size() == 0) {
                    rdList.add(rdNum);
                } else {
                    if (rdList.contains(rdNum)) {
                        continue;
                    } else {
                        rdList.add(rdNum);
                    }
                }
                if (rdList.size() == 6) {
                    break;
                }
            }
            Collections.sort(rdList);
            String blueBallResult = "";

            for (int i = 0; i < rdList.size(); i++) {
                blueBallResult += blueBalls[rdList.get(i)];
                if (i != rdList.size() - 1) {
                    blueBallResult += ",";
                }
            }
            System.out.println("选中蓝球为：" + blueBallResult);

            final Integer redBall = redBalls[rd.nextInt(16)];
            System.out.println("选中红球为：" + redBall);
            //保存到 last_balls
            final LastBallsDTO lastBallsDTO = new LastBallsDTO();
            lastBallsDTO.setCountNum(countNum);
            lastBallsDTO.setUserName(login);
            lastBallsDTO.setBlueBalls(blueBallResult);
            lastBallsDTO.setRedBall(redBall.toString());

            System.out.println(lastBallsDTO);

            //开奖只能开1次，检验是否已经有这个用户和期数的开奖记录，如果有，就不操作
            final List<LastBallsDTO> latestRightBallList = lastBallsMapper.lastBalls(login);
            if (latestRightBallList.size() != 0) {
                final LastBallsDTO latestRightBall = latestRightBallList.get(0);
                if (latestRightBall.getCountNum() != lastBallsDTO.getCountNum()) {
                    lastBallsMapper.insertOne(lastBallsDTO);
                }else {
                    //相同的期数 do nothing
                }
            }else {
                lastBallsMapper.insertOne(lastBallsDTO);
            }
        }
        return AjaxResult.success();
    }

    /**
     * 红蓝球 购买 按钮
     * @param dto BuyRecordDTO
     * @return
     */
    @PostMapping("/buyLottery")
    public AjaxResult buyLottery(@RequestBody BuyRecordDTO dto) {

        System.out.println("buyLottery");
        final String username = SecurityUtils.getUsername();
        dto.setUserName(username);
        final List<LastBallsDTO> lastBallsDTOS = lastBallsMapper.lastBalls(username);
        final int countNum = lastBallsDTOS.get(0).getCountNum();
        dto.setCountNum(countNum+1);

        //给蓝球排序，从小到大:1,2,3,4,5,6
        final String[] split = dto.getBlueBalls().split(",");
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            final Integer num = Integer.valueOf(split[i].trim());
            integerList.add(num);
        }
        Collections.sort(integerList);
        String sortedBlueBalls = integerList.toString();
        sortedBlueBalls = sortedBlueBalls.replace("[", "").replace("]", "");
        dto.setBlueBalls(sortedBlueBalls);

        System.out.println("排序后的蓝球："+sortedBlueBalls);

//        buyRecordMapper.insertOne(dto);
        //购买需要扣减个人积分
        final int buyCount = dto.getBuyCount();
        //2积分1注，
        int reduceNum = buyCount * 2;
        if (username.equals("admin")) {
            return AjaxResult.error("管理员账号不能使用此功能！");
        }
        final List<UserScoreDTO> userScoreDTOList = lastBallsMapper.queryScore(username);
        if (userScoreDTOList.size() == 2) {
            final UserScoreDTO userScoreDTO = userScoreDTOList.get(1);
            if (userScoreDTO.getUserScore() - reduceNum < 0) {
                return AjaxResult.error("此账号积分不足以买这么多注票");
            }else {
                userScoreDTO.setUserScore(userScoreDTO.getUserScore() - reduceNum);
                int changeCol = lastBallsMapper.updateUserScore(userScoreDTO);
                int changeTotalScore = lastBallsMapper.updateTotalScore(reduceNum);
                if (changeCol != 1) {
                    return AjaxResult.error("更新此用户积分时异常！");
                }
                if (changeTotalScore != 1) {
                    return AjaxResult.error("更新总积分时异常！");
                }
                buyRecordMapper.insertOne(dto);
            }
        }else {
            return AjaxResult.error("没查到该用户积分信息！");
        }

        return AjaxResult.success(dto);
    }

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public AjaxResult list(SysDept dept)
    {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return success(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId)
    {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        depts.removeIf(d -> d.getDeptId().intValue() == deptId || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""));
        return success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId)
    {
        deptService.checkDeptDataScope(deptId);
        return success(deptService.selectDeptById(deptId));
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept)
    {
        if (!deptService.checkDeptNameUnique(dept))
        {
            return error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(getUsername());
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept)
    {
        Long deptId = dept.getDeptId();
        deptService.checkDeptDataScope(deptId);
        if (!deptService.checkDeptNameUnique(dept))
        {
            return error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        else if (dept.getParentId().equals(deptId))
        {
            return error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        }
        else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus()) && deptService.selectNormalChildrenDeptById(deptId) > 0)
        {
            return error("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(getUsername());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId)
    {
        if (deptService.hasChildByDeptId(deptId))
        {
            return warn("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId))
        {
            return warn("部门存在用户,不允许删除");
        }
        deptService.checkDeptDataScope(deptId);
        return toAjax(deptService.deleteDeptById(deptId));
    }
}
