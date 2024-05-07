<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="24">
        <h1><span>【奖池总积分：{{totalScore}}】</span>  <span>【个人积分：{{userScore}}】</span></h1>
      </el-col>
      <el-col :sm="24" :lg="24">
        <blockquote class="text" style="font-size: 14px; color: black">

          <h2>查询区域</h2>
              <el-button type="primary" icon="el-icon-edit" disabled>请输入期数后操作</el-button>
              <el-input-number v-model="inputCountNum" @change="handleInputCountChange" :min="1" :max="65535" label="选择期数后查询"></el-input-number>
              <el-button @click="getRightBallByCountNum(inputCountNum)" type="primary" icon="el-icon-search">按期数查询</el-button>
              <el-button @click="checkRightBalls(inputCountNum)" type="success" icon="el-icon-search">按期数兑奖</el-button>


<!--          :filters="[{text: 1, value: '2016-05-01'}, {text: '2016-05-02', value: '2016-05-02'}, {text: '2016-05-03', value: '2016-05-03'}, {text: '2016-05-04', value: '2016-05-04'}]"-->

          <el-col :sm="24" :lg="12" style="padding-left: 50px;" >
            <el-table
              :data="tableData"
              border
              style="width: 100%" id="lastBallTable">
              <el-table-column
                prop="countNum"
                label="开奖期数"
                  width="90">
              </el-table-column>
              <el-table-column
                prop="blueBalls"
                label="蓝球"
                width="180">
              </el-table-column>
              <el-table-column
                prop="redBall"
                label="红球">
              </el-table-column>
<!--              <el-table-column-->
<!--                prop="buyCount"-->
<!--                label="备注">-->
<!--              </el-table-column>-->
<!--              <el-table-column-->
<!--                prop="other"-->
<!--                label="">-->
<!--              </el-table-column>-->
            </el-table>

            <el-table
              :data="tableData2"
              border
              style="width: 100%" id="lastBallTable">
              <el-table-column
                prop="countNum"
                label="购买期数"
                width="90">
              </el-table-column>
              <el-table-column
                prop="blueBalls"
                label="蓝球"
                width="180">
              </el-table-column>
              <el-table-column
                prop="redBall"
                label="红球"
                width="50">
              </el-table-column>
              <el-table-column
                prop="buyCount"
                label="注数" width="50">
              </el-table-column>
              <el-table-column
                prop="rightBallFlag"
                label="是否中奖" width="80">
              </el-table-column>
              <el-table-column
                prop="checkScoreFlag"
                label="是否兑奖">
              </el-table-column>
            </el-table>

          </el-col>


          <el-col :sm="24" :lg="12" style="padding-left: 50px;" >
            <el-row>
              <el-col :span="10">
                <ul>
                  <li>红篮球游戏</li>
                  <li>简介：蓝色球号码区由1-33共三十三个号码组成，红色球号码区由1-16共十六个号码组成。从33个蓝球随机选取6个，从16个红球随机选取1个，组成中奖号码。</li>
                  <li>中奖图示</li>
                  <li><span>一等奖，5000000积分</span><span style="color: blue">●●●●●●</span><span style="color: red">●</span></li>
                  <li><span>二等奖，1000000积分</span><span style="color: blue">●●●●●●</span><span>○</span></li>
                  <li><span>三等奖，3000积分</span><span style="color: blue">●●●●●○</span><span style="color: red">●</span></li>
                  <li><span>四等奖，200积分</span><span style="color: blue">●●●●○○</span><span style="color: red">●</span>或<span style="color: blue">●●●●●○</span><span>○</span></li>
                  <li><span>五等奖，10积分</span><span style="color: blue">●●●●○○</span><span>○</span>或<span style="color: blue">●●●○○○</span><span style="color: red">●</span></li>
                  <li><span>六等奖，5积分</span><span style="color: blue">●●○○○○</span><span style="color: red">●</span>或<span style="color: blue">●○○○○○</span><span style="color: red">●</span>或<span style="color: blue">○○○○○○</span><span style="color: red">●</span></li>
                  <li><el-button type="success">查询历史开奖记录（暂未开发）</el-button></li>
                </ul>
              </el-col>
            </el-row>
          </el-col>
          <br />
          <br />
        </blockquote>

        <hr />
      </el-col>
    </el-row>
    <el-row :gutter="20">

      <div>
        <h2>购买区域</h2>
        <h4>每期您能够购买10注，每注消耗2积分</h4>
        <el-form ref="form" :model="form" label-width="120px">
          <el-form-item label="蓝球(6个，1-33)" label-width="140px">
            <el-input
              v-for="(item, index) in form.blueBalls"
              :key="index"
              v-model="form.blueBalls[index]"
              type="number"
              min="1"
              max="33"
              placeholder="1-33"
              style="width: 80px; margin-right: 10px;"
            ></el-input>
          </el-form-item>
          <el-form-item label="红球(1个，1-16)"  label-width="140px">
            <el-input
              v-model="form.redBall"
              type="number"
              min="1"
              max="16"
              placeholder="1-16"
              style="width: 80px;"
            ></el-input>
          </el-form-item>
          <el-form-item label="注数，默认为1注"  label-width="140px">
            <el-input
              v-model="form.buyCount"
              type="number"
              min="1"
              value="1"
              placeholder="注数"
              style="width: 80px;"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="buyLottery">购买</el-button>
            <el-button type="success" @click="makeRightBalls">开奖</el-button>
          </el-form-item>
        </el-form>

        <div v-if="selectedNumbers && selectedNumbersFlag">
          <h3>您选择的号码：</h3>
          <p>蓝球：{{ selectedNumbers.blueBalls.join(', ') }}</p>
          <p>红球：{{ selectedNumbers.redBall }}</p>
          <p>注数：{{ selectedNumbers.buyCount }}</p>
        </div>
      </div>

<!--      <el-col :sm="24" :lg="12" style="padding-left: 20px">-->
<!--        <h2>若依后台管理框架</h2>-->
<!--        <p>-->
<!--          一直想做一款后台管理系统，看了很多优秀的开源项目但是发现没有合适自己的。于是利用空闲休息时间开始自己写一套后台系统。如此有了若依管理系统，她可以用于所有的Web应用程序，如网站管理后台，网站会员中心，CMS，CRM，OA等等，当然，您也可以对她进行深度定制，以做出更强系统。所有前端后台代码封装过后十分精简易上手，出错概率低。同时支持移动客户端访问。系统会陆续更新一些实用功能。-->
<!--        </p>-->
<!--        <p>-->
<!--          <b>当前版本:</b> <span>v{{ version }}</span>-->
<!--        </p>-->
<!--        <p>-->
<!--          <el-tag type="danger">&yen;免费开源</el-tag>-->
<!--        </p>-->
<!--        <p>-->
<!--        </p>-->
<!--      </el-col>-->

    </el-row>
    <el-divider />
<!--    <el-row :gutter="20">-->
<!--      <el-col :sm="24" :lg="12" style="padding-left: 20px">-->
<!--        <h2>hello </h2>-->
<!--      </el-col>-->
<!--    </el-row>-->
  </div>
</template>

<script>
import { listUser, getUser, delUser, addUser, updateUser, resetUserPwd, changeUserStatus, deptTreeSelect } from "@/api/system/user";
import {lastBall, getRightBallByCountNum, getBuyRecordList, makeRightBalls, buyLottery, checkRightBalls, queryScore} from "@/api/system/redBlueBall";
import log from "@/views/monitor/job/log";

export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "3.8.7",
      tableData: [{
        countNum: 0,
        blueBalls: '蓝球异常',
        redBall: '红球异常'
      },
      ],
      //
      tableData2: [
      ],
      // filterCountNum : [{text: 1, value: 1}, {text: 2, value: 2}, {text: 3, value: 3},],
      inputCountNum : 1,
      totalScore : 350000000,
      userScore : 0,

      form: {
        blueBalls: Array(6).fill(''), // 初始化6个空的红球输入框
        redBall: '',
        buyCount: 1,
      },
      selectedNumbers: null, // 用于存储用户选择的号码
      selectedNumbersFlag : false,
    };
  },
  created() {
    this.getLastBalls();
    this.getRightBallByCountNum(this.inputCountNum);
    this.queryScore();
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },

    getLastBalls() {
      console.log('=============testBtn=============');
      lastBall().then(res => {
        console.log(res);
        // this.tableData[0].countNum = res.data.countNum;
        // this.tableData[0].blueBalls = res.data.blueBalls;
        // this.tableData[0].redBall = res.data.redBall;
        this.tableData = res.data
      });
    },
    getRightBallByCountNum(v) {
      console.log('=============getRightBallByCountNum=============');
      console.log(v);
      getRightBallByCountNum(v).then(res => {
        console.log(res);
        this.tableData = res.data
      });
      this.getBuyRecordList(v);
      // for (let j = 0; j < this.tableData2.length; j++) {
      //   for (let i = 0; i < this.tableData.length; i++) {
      //     var rightBlueBalls = this.tableData[i].blueBalls;
      //     var rightRedBall = this.tableData[i].redBall;
      //     this.tableData2[j].blueBalls === thi
      //   }
      // }
    },

    //给购买期数table赋值
    getBuyRecordList(v) {
      console.log('=============getBuyRecordList=============');
      getBuyRecordList(v).then(res => {
        console.log('=============getBuyRecordList返回值=============');
        console.log(res);
        this.tableData2 = res.data
      })
    },

    handleInputCountChange(v) {
      console.log(v)
    },
    // filterHandler(value, row, column) {
    //   const property = column['property'];
    //   return row[property] === value;
    // }

  //购买按钮
    buyLottery() {
      this.selectedNumbers = {
        blueBalls: this.form.blueBalls.filter(n => n && !isNaN(n)), // 过滤空和非数字
        redBall: this.form.redBall,
        buyCount: this.form.buyCount,
      };
      console.log("=====1111111================")
      var temp = this.selectedNumbers.blueBalls.join(', ')

      let validNumArray = this.selectedNumbers.blueBalls;
      let validRedBall = this.selectedNumbers.redBall;
      validNumArray = validNumArray.map(item => Number(item));
      validNumArray.forEach(item => {
        if(item < 1 || item > 33){
          // alert("蓝球数字区间在1-33")
          this.$message.error('蓝球数字区间在1-33')
          this.selectedNumbersFlag=false
          return;
        }
      });

      if (validRedBall) {
        if (Number(validRedBall) < 1 || Number(validRedBall) > 16) {
          // alert("红球数字区间在1-16")
          this.$message.error('红球数字区间在1-16')
          this.selectedNumbersFlag=false
          return;
        }
      }else if (!(validRedBall instanceof Number)) {
        // alert("红球数字要选中1个")
        this.$message.error('红球数字要选中1个')
        this.selectedNumbersFlag=false
        return;
      }

      if (this.selectedNumbers.buyCount < 1) {
        // alert("注数必须大于0")
        this.$message.error('注数必须大于0')
        this.selectedNumbersFlag=false
        return;
      }

      let uniqueBlueBallsSet = new Set(this.selectedNumbers.blueBalls);

      if(this.selectedNumbers.blueBalls.length != uniqueBlueBallsSet.size) {
        // alert("蓝球数字不能重复！")
        this.$message.error('蓝球数字不能重复！')
        this.selectedNumbersFlag=false
        return;
      }else if (uniqueBlueBallsSet.size != 6) {
        // alert("蓝球数字要选中6个")
        this.selectedNumbersFlag=false
        this.$message.error('蓝球数字要选中6个')
        return;
      }


      console.log(temp)
      this.selectedNumbers.blueBallsStr = temp;
      this.selectedNumbersFlag = true;
      console.log(this.selectedNumbers)
      console.log("=====22222222===============")

      //校验参数以后，可以调用接口了
      console.log("校验参数以后，可以调用接口了")
      // this.selectedNumbers.blueBalls = this.selectedNumbers.blueBallsStr
      let dataParam = {
          blueBalls: this.selectedNumbers.blueBallsStr,
          redBall: this.selectedNumbers.redBall,
          buyCount: this.selectedNumbers.buyCount,
      };
      buyLottery(dataParam).then(res =>{
        console.log("#########buyLottery request#######")
        console.log(res)
        this.$message({
          message: res.msg,
          type: 'success'
        });

      })
    },

    //按期数兑奖
    checkRightBalls(param) {

      console.log("checkRightBalls兑奖"+ param)
      checkRightBalls(param).then(res => {
        console.log(res)
        this.getBuyRecordList(param)
        this.$message({
          message: '按期数兑奖成功',
          type: 'success'
        });
      })


    },
    //开奖
    makeRightBalls() {

      console.log("makeRightBalls开奖")
      makeRightBalls().then(res => {
        console.log(res)
        this.$message({
          message: '开奖成功',
          type: 'success'
        });
        this.getLastBalls();
        this.getBuyRecordList(this.inputCountNum)
        // this.getRightBallByCountNum(this.inputCountNum);

      })

    },

    queryScore() {
      console.log("queryScore查询积分")
      queryScore().then(res => {
        this.totalScore = res.data[0].userScore;
        console.log("queryScore查询积分，总积分："+this.totalScore)
        if(res.data.length > 1 ) {
          this.userScore = res.data[1].userScore;
          console.log("queryScore查询积分，个人积分："+this.userScore)
        }
      })
    },


  }
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
    border: 1px solid black;
  }

  ul {
    padding: 0;
    margin: 0;
    border: 1px solid black; /* 设置边框 */
    background-color: #f5f7fa; /* 设置背景颜色 */
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  li {
    border: 1px solid black; /* 设置边框 */
    background-color: #f5f7fa; /* 设置背景颜色 */
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }

  .custom-col {
    border: 1px solid black; /* 设置边框 */
    background-color: #f5f7fa; /* 设置背景颜色 */
    /* 其他你想要的样式 */
  }
}
</style>

