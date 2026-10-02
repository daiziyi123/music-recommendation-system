<!--
 * 页面头部
 *
-->
<template>
    <div class="the-header">
      <div class="header-logo" @click="goHome">
          <img :src="require('../assets/img/OIP-C (2).jpg')" style="height: 95%;">
          <div style="color: rgb(64, 158, 255);margin-top: -95px;margin-left: 70px"  >南湖音乐网</div>
      </div>
      <ul class="navbar">
          <li :class="{active: item.name == activeName}" v-for="item in navMsg" :key="item.path" @click="goPage(item.path,item.name)">
              {{item.name}}
          </li>
          <li>
              <div class="header-search">
                  <input type="text" placeholder="搜索音乐" @keyup.enter="goSearch()" v-model="keywords">
                  <div class="search-btn" @click="goSearch()">
                      <i class="iconfont icon-search-button" style="font-size: 28px;color:white;"></i>
                  </div>
              </div>
          </li>
          <li v-show="!loginIn" :class="{active: item.name == activeName}" v-for="item in loginMsg" :key="item.path" @click="goPage(item.path,item.name)">
              {{item.name}}
          </li>
          <li class="become_vip" @click="showVipDialog = true">
              成为VIP
          </li>
      </ul>
      <div class="header-right" v-show="loginIn">
          <div id='user'>
              <img :src='attachImageUrl(avator)'>
          </div>
          <ul class="menu">
              <li v-for="(item,index) in menuList" :key="index" @click="goMenuList(item.path)">{{item.name}}</li>
          </ul>
      </div>

      <!-- VIP购买弹窗 -->
      <div class="vip-dialog" v-show="showVipDialog" @click="showVipDialog = false">
        <div class="dialog-content" @click.stop>
          <h3>扫码支付成为VIP会员</h3>
          <div class="qrcode-container">
            <img src="../assets/img/swiper/qq.jpg" alt="VIP支付二维码">
          </div>
          <div class="dialog-actions">
            <button class="cancel-btn" @click="showVipDialog = false">取消</button>
            <button class="confirm-btn" @click="handlePayment">已支付</button>
          </div>
        </div>
      </div>
    </div>
  </template>

  <script>
  import {mapGetters} from 'vuex';
  import {navMsg,loginMsg,menuList} from '../assets/data/header';

  export default {
    name: 'the-header',
    data() {
        return {
            navMsg: [],    //左侧导航栏
            keywords: '',  //搜索关键字
            loginMsg: [],  //右侧导航栏
            menuList: [],  //用户下拉菜单
            showVipDialog: false // VIP弹窗显示控制
        }
    },
    computed:{
        ...mapGetters([
            'activeName',
            'loginIn',
            'avator'
        ])
    },
    created() {
        this.navMsg = navMsg;
        this.loginMsg = loginMsg;
        this.menuList = menuList;
    },
    mounted(){
        document.querySelector('#user').addEventListener('click',function(e){
            document.querySelector('.menu').classList.add("show");
            e.stopPropagation()           //关键在于阻止冒泡
        },false);
        document.querySelector('.menu').addEventListener('click',function(e){
            e.stopPropagation()           //点击菜单内部时，阻止时间冒泡，这样，点击内部时，菜单不会关闭
        },false);
        document.addEventListener('click',function(){
          document.querySelector('.menu').classList.remove('show');
        },false);
    },

    methods: {
         //提示信息
          notify(title,type) {
              this.$notify({
                  title: title,
                  type: type
              })
          },
        goHome() {
            this.$router.push({path: "/"});
        },
        goPage(path,name) {
            if(!this.loginIn && path=='/my-music'){
                this.notify('请先登录','warning');
            }else{
              this.$store.commit('setActiveName',name);
              this.$router.push({path: path});
            }
        },
        goSearch(){
            this.$router.push({path:'/search',query:{keywords: this.keywords}})
        },
        //获取图片地址
        attachImageUrl (srcUrl) {
          return srcUrl? this.$store.state.configure.HOST+srcUrl : '../assets/img/user.jpg';
        },
        goMenuList(path){
            if(path == 0){
                this.$store.commit('setLoginIn',false);
                this.$store.commit('setIsActive',false);
                this.$router.go(0);
            }else{
                this.$router.push({path:path});
            }
        },
        // 处理支付成功逻辑

        handlePayment() {
          this.showVipDialog = false;
          this.notify('支付成功！','success');
          // 这里可以添加实际的支付成功后的逻辑
          // 例如：更新用户VIP状态、跳转到会员中心等
        },

    }

  }
  </script>

  <style lang="scss" scoped>
  @import '../assets/css/the-header.scss';

  // 添加VIP弹窗样式
  .vip-dialog {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 999;

    .dialog-content {
      background-color: white;
      border-radius: 8px;
      padding: 20px;
      width: 300px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);

      h3 {
        text-align: center;
        margin-bottom: 15px;
      }

      .qrcode-container {
        text-align: center;
        margin-bottom: 20px;

        img {
          width: 200px;
          height: 200px;
          object-fit: cover;
        }
      }

      .dialog-actions {
        display: flex;
        justify-content: space-between;

        button {
          padding: 8px 16px;
          border: none;
          border-radius: 4px;
          cursor: pointer;
          font-size: 14px;
        }

        .cancel-btn {
          background-color: #f0f0f0;
          color: #333;
        }

        .confirm-btn {
          background-color: #4334a6;
          color: white;
        }
      }
    }
  }
  </style>
