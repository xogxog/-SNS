<template>
  <div>
    <v-card
      class="rounded-xl justify-center d-flex"
      @click.stop="noticeMove(recv.noticeContentId, recv.noticeType)"
    >
      <v-alert
        v-show="noticeAlarm && !dialog"
        transition="slide-y-transition"
        class="alert"
        close-icon
        elevation="24"
      >
        <div class="d-flex align-center">
          <ProfilePhoto
            :size="50"
            :imgUrl="recv.userProfileUrl"
            :userSeq="recv.userSeq"
          />
          <div class="mx-5">
            {{ recv.userName }}님이 {{ recv.noticeMessage }}
          </div>
        </div>
      </v-alert>
    </v-card>
    <v-app-bar
      color="rgb(98,101,232)"
      dark
      fixed
      hide-on-scroll
      max-width="450px"
      width="100%"
      class="align-space-center my-bar"
      height="80px"
      style="z-index: 7; left: calc(max(((100% - 450px) / 2), 0px))"
    >
      <img
        height="70"
        width="70"
        src="../../assets/logo2.png"
        alt="로고"
        @click.stop="
          $router.push({ name: 'Main', params: { groupId: nowGroup.groupId } })
        "
      />
      <v-spacer></v-spacer>
      <v-btn
        rounded
        text
        @click.stop="
          $router.push({ name: 'FeedCreate', params: { feedId: -1 } })
        "
        >추억담기
      </v-btn>

      <v-dialog v-model="dialog" scrollable max-width="400px">
        <template v-slot:activator="{ on, attrs }">
          <v-btn v-bind="attrs" v-on="on" icon>
            <v-badge
              v-if="unreadNoticeCount"
              color="red"
              :content="unreadNoticeCount"
            >
              <v-icon>notifications_none</v-icon>
            </v-badge>
            <v-icon v-else>notifications_none</v-icon>
          </v-btn>
        </template>
        <v-card class="rounded-xl pa-0">
          <v-card-title>
            <span>알림</span>
            <v-spacer></v-spacer>
            <v-btn text class="body-1" @click.stop="noticeReadAll()">
              모두읽기
            </v-btn>
          </v-card-title>
          <v-divider></v-divider>
          <v-card-text style="height: 500px">
            <v-container class="text-center mt-5" v-if="!noticeList.length"
              ><h3>알림이 없습니다.</h3>
            </v-container>
            <v-container
              class="px-0"
              v-for="(notice, $idx) in noticeList"
              :key="$idx"
              @click="
                noticeMove(notice.noticeContentId, notice.noticeType),
                  (dialog = false)
              "
            >
              <v-hover>
                <template v-slot:default="{ hover }">
                  <v-card
                    ripple
                    max-width="380px"
                    class="content-box mx-auto pa-3 transition-swing rounded-xl align-center d-flex"
                    :class="`elevation-${hover ? 20 : 6}`"
                    :disabled="notice.noticeReadYn == 'y'"
                    :color="notice.noticeReadYn == 'y' ? '#DDDDDD' : ''"
                    @click.stop="
                      noticeMove(notice.noticeContentId, notice.noticeType),
                        noticeRead(
                          notice.noticeId,
                          notice.noticeContentId,
                          notice.noticeType
                        ),
                        (notice.noticeReadYn = 'y'),
                        (dialog = false)
                    "
                  >
                    <div class="mx-3" style="width: 100%">
                      <div>
                        <ProfilePhoto
                          class="d-inline-flex"
                          :size="24"
                          :imgUrl="notice.userProfileImageUrl"
                          :userSeq="notice.userSeq"
                          @clicked="dialog = false"
                        />
                        {{ notice.userName }}님이 {{ notice.noticeMessage }}
                      </div>
                      <div class="my-2">
                        <v-icon class="ps-2" v-if="notice.noticeContentTitle"
                          >subdirectory_arrow_right</v-icon
                        >
                        {{ notice.noticeContentTitle }}
                      </div>
                      <div>
                        {{ notice.noticeContentContent }}
                      </div>
                      <div class="text-right">
                        {{ notice.noticeTime }}
                      </div>
                    </div>
                  </v-card>
                </template>
              </v-hover>
            </v-container>
            <infinite-loading @infinite="getNoticeList"></infinite-loading>
          </v-card-text>
          <v-divider></v-divider>
          <v-card-actions>
            <v-btn
              text
              class="body-1"
              color="rgb(98,101,232)"
              @click="dialog = false"
            >
              닫기
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
    </v-app-bar>

    <v-navigation-drawer
      v-model="drawer"
      fixed
      right
      temporary
      class="drawer-position"
    >
      <v-list nav dense>
        <v-list-item-group
          v-model="group"
          active-class="deep-purple--text text--accent-4"
        >
          <v-list-item
            class="my-5"
            @click.stop="
              $router.push({
                name: 'UserPage',
                params: { userSeq: userSeq },
              }),
                $router.app._route.name == 'UserPage' ||
                $router.app._route.name == 'UserPageScrap'
                  ? getProfile(userSeq)
                  : ''
            "
          >
            <ProfilePhoto :size="70" :imgUrl="userImgUrl" :userSeq="userSeq" />
            <h1 class="ml-7">{{ userName }}</h1>
          </v-list-item>
          <v-list-item
            @click.stop="
              $router.push({
                name: 'UserPage',
                params: { userSeq: userSeq },
              }),
                $router.app._route.name == 'UserPage' ||
                $router.app._route.name == 'UserPageScrap'
                  ? getProfile(userSeq)
                  : ''
            "
          >
            <v-list-item-title class="d-flex align-center">
              <v-icon class="mx-3">account_circle</v-icon>
              <h1>내 정보</h1>
            </v-list-item-title>
          </v-list-item>
          <v-list-item @click.stop="move('GroupProfile')">
            <v-list-item-title class="d-flex align-center">
              <v-icon class="mx-3">groups</v-icon>
              <h1>그룹 프로필</h1>
            </v-list-item-title>
          </v-list-item>
          <v-list-item @click.stop="move('Select')">
            <v-list-item-title class="d-flex align-center">
              <v-icon class="mx-3">switch_account</v-icon>
              <h1>그룹 목록</h1>
            </v-list-item-title>
          </v-list-item>
          <!-- <v-list-item @click.stop="move('Main')">
            <v-list-item-title class="d-flex align-center">
              <v-icon class="mx-3">mdi-history</v-icon>
              <h1>게시물</h1>
            </v-list-item-title>
          </v-list-item>
          <v-list-item @click.stop="move('Timeline')">
            <v-list-item-title class="d-flex align-center">
              <v-icon class="mx-3">mdi-heart</v-icon>
              <h1>추억여행</h1>
            </v-list-item-title>
          </v-list-item>
          <v-list-item @click.stop="move('Calendar')">
            <v-list-item-title class="d-flex align-center">
              <v-icon class="mx-3">mdi-map-marker</v-icon>
              <h1>일정표</h1>
            </v-list-item-title>
          </v-list-item> -->
          <v-list-item @click.stop="move('EntFeedList')">
            <v-list-item-title class="d-flex align-center">
              <v-icon class="mx-3">extension</v-icon>
              <h1>투표/미니게임</h1>
            </v-list-item-title>
          </v-list-item>

          <!-- <v-list-item @click.stop="move('Management')">
            <v-list-item-title class="d-flex align-center">
              <v-icon class="mx-3">manage_accounts</v-icon>
              <h1>그룹관리</h1>
            </v-list-item-title>
          </v-list-item> -->
          <hr style="margin: auto; border-top: 1px solid #f0f0f0" />
          <v-list-item @click.stop="logout()">
            <v-list-item-title class="d-flex align-center">
              <v-icon class="mx-3">logout</v-icon>
              <h1>로그아웃</h1>
            </v-list-item-title>
          </v-list-item>
        </v-list-item-group>
      </v-list>
      <div style="position: absolute; bottom: 0px; width: 100%" class="pa-5 d-flex justify-center">
        <div>

        <span class="d-flex justify-end">SSAFY 공통PJT.</span>
        <p class="d-flex justify-end">© 2022. 우家우가.</p>
        </div>
      </div>
    </v-navigation-drawer>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import ProfilePhoto from "../ProfilePhoto.vue";
export default {
  name: "FrontendNavbar",

  data: () => ({
    dialog: false,
    drawer: false,
    group: null,
  }),
  components: {
    ProfilePhoto,
  },
  watch: {
    group() {
      this.drawer = false;
    },
    dialog() {
      this.setDialog();
    },
  },

  mounted() {
    this.connect();
    this.getNoticeList();
  },
  beforeDestroy() {
    this.disconnect();
  },

  methods: {
    ...mapActions("account", ["logout"]),
    ...mapActions("notice", [
      "noticeReadAll",
      "noticeRead",
      "getNoticeList",
      "connect",
      "disconnect",
      "send",
      "setDialog",
    ]),
    ...mapActions("group", ["getProfile"]),
    feedCreate() {
      this.$router.push({ name: "FeedCreate" });
    },
    noticeMove(noticeContentId, noticeType) {
      console.log(noticeType);
      switch (noticeType) {
        case "feedCreate":
        case "commentCreate":
        case "emotionCreate":
        case "shareCreate":
          this.$router.push({
            name: "Detail",
            params: { feedId: noticeContentId },
          });
          break;

        case "voteCreate":
        case "voteEnd":
          this.$router.push({
            name: "EntFeedDetail",
            params: {
              contentType: "vote",
              contentId: noticeContentId,
            },
          });
          break;

        case "ghostLegCreate":
          this.$router.push({
            name: "EntFeedDetail",
            params: {
              contentType: "ghostLeg",
              contentId: noticeContentId,
            },
          });
          break;
        default:
      }
    },
    move(page) {
      this.$router.push({ name: page });
    },
  },
  computed: {
    ...mapState("account", ["userSeq", "userImgUrl", "userName", "nowGroup"]),
    ...mapState("notice", [
      "noticeAlarm",
      "recv",
      "noticeList",
      "unreadNoticeCount",
    ]),
    nowMobile() {
      if (window.innerWidth < 450) {
        return true;
      } else {
        return false;
      }
    },
  },
};
</script>

<style scoped>
.drawer-position {
  right: calc((100vw - 480px) / 2);
}
.my-bar {
  left: calc((100vw - 450px) / 2);
}
.alert {
  position: fixed;
  top: 10px;
  z-index: 20;
}
</style>
