<template>
  <v-dialog max-width="600px" persistent v-model="dialog">
    <v-card>
      <v-card-title>
        <h3>일정 추가</h3>
      </v-card-title>
      <v-card-text>
        <v-form class="px-3" ref="form">
          <v-text-field label="일정" v-model="event.scheduleTitle" prepend-icon="mdi-folder-marker" :rules="inputRules">
          </v-text-field>
          <v-textarea label="상세설명" v-model="event.scheduleContent" prepend-icon="mdi-pencil" :rules="inputRules">
          </v-textarea>
          <v-row>
            <v-col cols="6" class="pb-0">
              <v-menu>
                <template v-slot:activator="{on}">

                  <v-text-field slot="activator" label="시작일" readonly prepend-icon="mdi-calendar-month" v-on="on"
                                :value="event.scheduleDateStart" class=""></v-text-field>
                </template>
                <v-date-picker v-model="event.scheduleDateStart"></v-date-picker>
              </v-menu>
            </v-col>
            <v-col cols="6" class="pb-0">
              <v-menu :close-on-content-click="false" v-model="startTimer" offset-y>
                <template v-slot:activator="{ on }">
                  <v-text-field label="시작 시간" readonly :value="event.scheduleTimeStart" prepend-icon="mdi-timer" v-on="on">
                  </v-text-field>
                </template>
                <v-time-picker v-if="startTimer" v-model="event.scheduleTimeStart">
                  <v-btn class="mx-auto" @click="selectTime">선택
                  </v-btn>
                </v-time-picker>
              </v-menu>
            </v-col>
          </v-row>

          <v-row>
            <v-col cols="6" class="pt-0">
              <v-menu>
                <template v-slot:activator="{on}">
                  <v-text-field slot="activator" label="종료일" readonly prepend-icon="mdi-calendar-month" v-on="on"
                                :value="event.scheduleDateEnd" class=""></v-text-field>
                </template>
                <v-date-picker v-model="event.scheduleDateEnd" :allowed-dates="allowedDates"></v-date-picker>
              </v-menu>
            </v-col>
            <v-col cols="6" class="pt-0">
              <v-menu :close-on-content-click="false" v-model="endTimer" offset-y>
                <template v-slot:activator="{ on }">
                  <v-text-field label="종료 시간" readonly :value="event.scheduleTimeEnd" prepend-icon="mdi-timer" v-on="on">
                  </v-text-field>
                </template>
                <v-time-picker v-if="endTimer" v-model="event.scheduleTimeEnd">
                  <v-btn class="mx-auto" @click="selectTime">선택
                  </v-btn>
                </v-time-picker>
              </v-menu>
            </v-col>
          </v-row>

          <div class="text-center">
            <v-btn text class="primary white--text mx-2 mt-3" @click="submit">
              추가
            </v-btn>
            <v-btn text class="primary white--text mx-2 mt-3" @click="close">
              닫기
            </v-btn>
          </div>
        </v-form>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data() {
    return {
      startTimer: false,
      endTimer: false,
    }
  },

  computed: {
    dialog() {
      return this.$store.state.calendar.dialog;
    },
    // calendar() {
    //   return this.$store.state.calendar.calendar;
    // },
    events() {
      return this.$store.state.calendar.events;
    },
    event() {
      return this.$store.state.calendar.event;
    },
  },

  methods: {
    submit() {
      if (this.event.scheduleTitle === '' || this.event.scheduleDateEnd === '') {
        console.log('에러')
        // store.commit('SET_SNACKBAR',
        //  setSnackBarInfo('제목과 종료일자를 작성해주세요.', 'error', 'top'));
      } else {
        // console.log(this.event)

        location.reload();
        this.$store.dispatch('REQUEST_ADD_EVENT', this.event);
      }
    },
    // submit() {
    //   if (this.$refs.form.validate()) {
    //     console.log(this.event)
    //     this.$store.dispatch('REQUEST_ADD_EVENT', this.events);
    //   }
    // },

    close() {
      this.$store.commit('CLOSE_CALENDAR_DIALOG');
      console.log(this.dialog)
    },

    selectTime() {
      this.endTimer = false;
      this.startTimer = false;
    },

    allowedDates(val) {
      let scheduleDateEnd = val.split('-').reduce((a, b) => a + b);
      let scheduleDateStart = this.event.scheduleDateStart.split('-').reduce((a, b) => a + b);
      return scheduleDateEnd >= scheduleDateStart;
    }
  },
}
</script>