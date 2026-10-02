<!--
 *  首页统计页面
-->
<template>
  <div style="padding: 30px;">
    <el-row :gutter="20" class="mgb20" type="flex" justify="center" align="stretch"> <!-- 关键修改：align="stretch" -->
      <!-- 左侧统计卡片 -->
      <el-col :span="14" style="display: flex; flex-direction: column;"> <!-- 关键修改：flex布局 -->
        <el-row :gutter="50" style="flex: 1;"> <!-- 关键修改：flex: 1 撑满高度 -->
          <!-- 用户总数 -->
          <el-col :span="24" class="mgb20">
            <el-card style="height: 100%;"> <!-- 关键修改：height: 100% -->
              <div class="grid-content" >
                <div class="grid-cont-center" style="color: #DC143C;">
                  <div class="grid-num">{{consumerCount}}</div>
                  <div><i class="iconfont icon-r-team" style="font-size: 25px;"> 用户总数</i></div>
                </div>
              </div>
            </el-card>
          </el-col>
          <!-- 歌曲总数 -->
          <el-col :span="24">
            <el-card style="height: 100%;"> <!-- 关键修改：height: 100% -->
              <div class="grid-content">
                <div class="grid-cont-center" style="color: #6495ED;">
                  <div class="grid-num">{{songCount}}</div>
                  <div>
                    <i class="iconfont icon-r-paper" style="font-size: 25px;"> 歌曲总数</i></div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-col>

      <!-- 右侧性别比例 -->
      <el-col :span="10">
        <div style="height: 100%; display: flex; flex-direction: column;">
          <h3 class="mgb20"><i class="iconfont icon-r-find" style="font-size: 28px;"></i> 用户性别比例</h3>
          <div style="background-color:white; flex: 1;">
            <ve-pie :data="consumerSex" :theme="options" style="height: 40px;margin-top: -50px"></ve-pie>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import {getAllConsumer,allSong,getAllSinger,getAllSongList} from '../api/index';
export default {
    data(){
        return {
            consumerCount: 0,       //用户总数
            songCount: 0,           //歌曲总数
            singerCount: 0,         //歌手数量
            songListCount: 0,        //歌单数量
            consumer: [],            //所有用户
            consumerSex:{           //按性别分类的用户数
                columns: ['性别','总数'],
                rows: [
                    {'性别': '男','总数': 0},
                    {'性别': '女','总数': 0}
                ]
            },
            options: {
                color: ['#87cefa','#ffc0cb']
            },
            options1: {
                color: ['yellow']
            },
            singerSex:{           //按性别分类的歌手数
                columns: ['性别','总数'],
                rows: [
                    {'性别': '女','总数': 0},
                    {'性别': '男','总数': 0},
                    {'性别': '组合','总数': 0},
                    {'性别': '不明','总数': 0}
                ]
            },
            country:{
                columns: ['国籍','总数'],
                rows: [
                    {'国籍': '中国','总数': 0},
                    {'国籍': '韩国','总数': 0},
                    {'国籍': '日本','总数': 0},
                    {'国籍': '美国','总数': 0},
                    {'国籍': '新加坡','总数': 0},
                    {'国籍': '意大利','总数': 0},
                    {'国籍': '马来西亚','总数': 0},
                    {'国籍': '西班牙','总数': 0}
                ]
            }
        }
    },
    created() {

    },
    mounted() {
        this.getConsumer();
        this.getSong();
        this.getSinger();
        this.getSongList();
    },
    methods: {
        getConsumer() {                     //用户总数
            getAllConsumer().then(res => {
                this.consumer = res;
                this.consumerCount = res.length;
                this.consumerSex.rows[0]['总数'] = this.setSex(1,this.consumer);
                this.consumerSex.rows[1]['总数'] = this.setSex(0,this.consumer);
            })
        },
        setSex(sex,val) {              //根据性别获取用户数
            let count = 0;
            for(let item of val){
                if(sex == item.sex){
                    count++;
                }
            }
            return count;
        },
        getSong() {                      //歌曲总数
            allSong().then(res => {
                this.songCount = res.length;
            })
        },
        getSinger() {                      //歌手数量
            getAllSinger().then(res => {
                this.singerCount = res.length;
                this.singerSex.rows[0]['总数'] = this.setSex(0,res);
                this.singerSex.rows[1]['总数'] = this.setSex(1,res);
                this.singerSex.rows[2]['总数'] = this.setSex(2,res);
                this.singerSex.rows[3]['总数'] = this.setSex(3,res);
                for(let item of res){
                    this.getByCountry(item.location);
                }
            })
        },

        getSongList() {                    //歌单数量
            getAllSongList().then(res => {
                this.songListCount = res.length;
                for(let item of res){
                    this.getByStyle(item.style);
                }
            })
        },
        getByStyle(style) {              //根据歌单风格获取数量
            for(let item of this.songStyle.rows){
                if(style.includes(item['风格'])){
                    item['总数']++;
                }
            }
        },
        getByCountry(location) {              //根据国籍获取数量
            for(let item of this.country.rows){
                if(location.includes(item['国籍'])){
                    item['总数']++;
                }
            }
        }
    }
}

</script>

<style scoped>
.grid-content {
    display: flex;
    align-items: center;
    height: 100px;
}

.grid-cont-center {
    flex: 1;
    text-align: center;
    font-size: 14px;
    color: darkgray;
}

.grid-num {
    font-size: 42px;
    font-weight: bold;
}
</style>
