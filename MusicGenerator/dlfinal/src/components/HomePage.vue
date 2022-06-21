<template>
  <v-app id="inspire">
    <v-app-bar
      app
      color="white"
      flat
    >
      <v-container class="py-0 fill-height">

        <h2>Classical Music Generator</h2>

        <v-spacer></v-spacer>
      </v-container>
    </v-app-bar>

    <v-main class="grey lighten-3">
      <v-container>
        <v-row>
          <v-col>
            <v-sheet
              min-height="70vh"
              rounded="lg"
              class="pa-15"
            >
              <v-select
                item-disabled="disable"
                item-text="name"
                :items="items"
                label="Choose Artist"
                v-model="selected"
              ></v-select>
              <v-btn
                @click="startDL()"
                :disabled="selected == ''"
              >
                Generate Random Song
              </v-btn>

            </v-sheet>
          </v-col>
        </v-row>
      </v-container>
      <v-checkbox
        class="d-none"
        v-model="loading"
        id="loadingbox"
      >
      </v-checkbox>
      <v-overlay :value="loading">
        <v-progress-circular
          indeterminate
          size="64"
        ></v-progress-circular>
      </v-overlay>
    </v-main>
  </v-app>
</template>

<script>
  export default {
    name: 'HomePage',
    data() {
      return {
        items: [
          {
          name: 'Beethoven',
          disable: false
          },
          {
          name: 'Bach',
          disable: true
          },
          {
          name: 'Tchaikovsky',
          disable: true
          },
          {
          name: 'Chopin',
          disable: true
          }
        ],
        selected: '',
        loading: false
      }
    },
    methods:{
      startDL(){
        this.download('http://127.0.0.1:5000/getmidi/'+this.selected);
      },
      download(file) {
        var request = new XMLHttpRequest();
        request.responseType = 'blob';
        request.open('GET', file);
        request.addEventListener('load', this.setLoading(true));
        request.addEventListener('load', function () {
          document.getElementById('loadingbox').click();
          var a = document.createElement('a');
          var url = URL.createObjectURL(request.response);
          a.href = url;
          a.download = "output.midi"
          a.click();
        });
        request.send();
      },
      setLoading(bool){
        this.loading = bool
      }
    }
  }
</script>
