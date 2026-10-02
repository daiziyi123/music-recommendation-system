<template>
  <div class="home">
    <div class="horizontal-layout">
      <div class="section" v-for="(item, index) in songsList" :key="index">
        <div class="section-header">
          <div class="category-tag">{{ index === 0 ? '歌单' : '歌手' }}</div>
          <div class="section-title">
            <i :class="item.icon"></i>
            <b>{{ item.name }}</b>
          </div>
        </div>
        <content-list :contentList="item.list"></content-list>
      </div>
    </div>
  </div>
</template>

<script>
import contentList from "../components/ContentList";
import { getAllSinger, getAllSongList } from "../api/index";
import { mapGetters } from 'vuex';

export default {
  name: "home",
  components: {
    contentList,
  },
  computed: {
    ...mapGetters([
      'listOfSongs',
      'userId',
    ])
  },
  data() {
    return {
      songsList: [
        // { name: "推荐歌单", list: [], icon: "iconfont icon-r-list" },
        // { name: "热门歌手", list: [], icon: "iconfont icon-r-love" },
        { name: "推荐歌单", list: [] },
        { name: "热门歌手", list: []},
      ],
    };
  },
  created() {
    this.getSongList(this.userId);
    this.getSinger();
  },
  methods: {
    getSongList(userId) {
      console.log("userId == " + userId);
      getAllSongList(userId)
        .then((res) => {
          this.songsList[0].list = res.slice(0, 3);
        })
        .catch((e) => {
          this.handleError(e);
        });
    },
    getSinger() {
      getAllSinger()
        .then((res) => {
          this.songsList[1].list = res.slice(0, 10);
        })
        .catch((e) => {
          this.handleError(e);
        });
    },
    handleError(e) {
      if (e.data == undefined) {
        if (e == "TypeError: Cannot read properties of undefined (reading 'status')") {
          e = "Network Error";
        }
        this.$message({
          showClose: true,
          message: e,
          type: "error",
          duration: 5000,
        });
      } else {
        this.$message({
          showClose: true,
          message: e.data,
          type: "error",
          duration: 5000,
        });
      }
    }
  },
};
</script>

<style lang="scss" scoped>
.home {
  margin-top: 60px;
  padding: 20px;
  width: 100%;
  box-sizing: border-box;
}

.horizontal-layout {
  display: flex;
  gap: 30px;
}

.section {
  flex: 1;
  min-width: 0;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.category-tag {
  font-size: 20px;
  font-weight: bold;
  color: #fff;
  background-color: #409EFF;
  padding: 5px 12px;
  border-radius: 4px;
  margin-right: 15px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 10px;

  i {
    font-size: 28px;
    color: #409EFF;
  }

  b {
    font-size: 24px;
    color: #333;
  }
}

@media (max-width: 768px) {
  .horizontal-layout {
    flex-direction: column;
    gap: 20px;
  }

  .section-header {
    margin-bottom: 10px;
  }
}
</style>
