<!--
 * 歌单列表页面
-->
<template>
  <div class="music-container">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <el-icon class="is-loading"><Loading /></el-icon>
      加载中...
    </div>

    <!-- 错误状态 -->
    <div v-if="error" class="error">
      <el-alert :title="error" type="error" show-icon />
      <el-button @click="fetchMusic">重试</el-button>
    </div>

    <!-- 音频播放器 -->
<!--    <audio ref="audioPlayer" controls class="audio-player"></audio>-->
    <!-- 音乐列表展示 -->
    <div v-if="!loading && !error" class="music-list">
      <div v-for="music in musicList" :key="music.id" class="music-card">
        <div class="music-cover">
          <el-image
            :src="music.pic"
            fit="cover"
            :preview-src-list="[music.pic]"
            :hide-on-click-modal="true">
            <template #error>
              <div class="image-error">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </div>

        <div class="music-info">
          <h3 class="music-title">{{ music.name }}</h3>
          <p class="music-intro">{{ music.introduction }}</p>

          <div class="music-actions">
            <el-button type="primary" @click="playMusic(music)">
              <el-icon><VideoPlay /></el-icon> 查看
            </el-button>

            <el-popover
              placement="right"
              width="400"
              trigger="click">
              <template #reference>
                <el-button>
                  <el-icon><Document /></el-icon> 歌词
                </el-button>
              </template>
              <div class="lyric-container">
                <h4>{{ music.name }} - 歌词</h4>
                <pre class="lyric-content">{{ music.lyric || '暂无歌词' }}</pre>
              </div>
            </el-popover>
            <el-button>
              删除
            </el-button>

          </div>
        </div>
      </div>
    </div>

    <!-- 分页控件 -->
    <el-pagination
      v-if="!loading && !error && total > 0"
      v-model:current-page="currentPage"
      :page-size="pageSize"
      layout="total, prev, pager, next, jumper"
      :total="total"
      @current-change="handlePageChange"
    />




  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { Message, MessageBox } from 'element-ui';

// 响应式数据
const musicList = ref([])
const loading = ref(false)
const error = ref(null)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const audioPlayer = ref(null)

// 获取音乐数据
const fetchMusic = async () => {
  try {
    loading.value = true
    error.value = null

    const response = await axios.get('http://localhost:9171/music/', {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })

    // 根据实际API响应结构调整
    musicList.value = response.data
    console.log(8989898989)
    console.log(response.data)
    console.log(8989898989)
    total.value = response.data.total || musicList.value.length

  } catch (err) {
    error.value = err.message || '获取音乐数据失败'
    Message.error(error.value)
  } finally {
    loading.value = false
  }
}

// 播放音乐
const playMusic = (music) => {
  if (audioPlayer.value) {

    audioPlayer.value.src = music.url
    console.log(5465465)
    console.log(music.url)
    console.log(5465465)
    audioPlayer.value.play()
    // Message.success(`正在播放: ${music.name}`)
  }
}

// 下载音乐
const downloadMusic = (url) => {
  const link = document.createElement('a')
  link.href = url
  link.download = url.split('/').pop() || 'music.mp3'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}
// 确认删除音乐
const confirmDelete = (music) => {
  MessageBox.confirm(
    `确定要删除歌曲《${music.name}》吗？此操作不可恢复！`,
    '删除确认',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning',
      center: true
    }
  ).then(async () => {
    await deleteMusic(music.id)
  }).catch(() => {
    Message.info('已取消删除')
  })
}

// 执行删除操作
const deleteMusic = async (id) => {
  try {
    loading.value = true
    await axios.delete(`http://localhost:9171/music/${id}`)
    Message.success('删除成功')
    fetchMusic() // 刷新列表
  } catch (err) {
    Message.error(`删除失败: ${err.message}`)
  } finally {
    loading.value = false
  }
}

// 分页变化
const handlePageChange = (newPage) => {
  currentPage.value = newPage
  fetchMusic()
}

// 组件挂载时获取数据
onMounted(fetchMusic)
</script>


<style scoped>

/* 隐藏滚动条但保留功能 */
.music-container::-webkit-scrollbar {
  display: none; /* Chrome/Safari */
}
.music-container {
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE/Edge */
}
/* 1. 禁用全局滚动条 */
html, body {
  overflow: hidden; /* 隐藏全局滚动条 */
  height: 100%;    /* 确保高度生效 */
}

/* 2. 容器设置固定高度 + 内部滚动 */
.music-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  height: 100vh;   /* 视口高度 */
  overflow-y: auto; /* 允许容器内部滚动 */
}

/* 3. 调整音乐列表高度 */
.music-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
  max-height: calc(100vh - 200px); /* 减去其他元素高度 */
  overflow-y: auto; /* 列表内部滚动 */
}
.music-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.loading {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  color: #666;
}

.error {
  text-align: center;
  padding: 20px;
}

.music-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
}

.music-card {
  display: flex;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s;
  background: #fff;
}

.music-card:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transform: translateY(-5px);
}

.music-cover {
  width: 120px;
  height: 120px;
  flex-shrink: 0;
}

.music-cover .el-image {
  width: 100%;
  height: 100%;
}

.image-error {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #909399;
}

.music-info {
  flex: 1;
  padding: 15px;
  display: flex;
  flex-direction: column;
}

.music-title {
  margin: 0 0 8px 0;
  font-size: 16px;
  color: #303133;
}

.music-intro {
  margin: 0 0 12px 0;
  font-size: 13px;
  color: #606266;
  flex: 1;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.music-actions {
  display: flex;
  gap: 8px;
}

.lyric-container {
  max-height: 400px;
  overflow-y: auto;
}

.lyric-content {
  white-space: pre-wrap;
  font-family: inherit;
  line-height: 1.6;
}
</style>


