from flask import Flask, send_file
from flask_cors import CORS
import music_generator_new

app = Flask(__name__)

# enable CORS
CORS(app, resources={r'/*': {'origins': '*'}})

@app.route("/getmidi/<composer>",methods=['GET'])
def getmidi(composer):
    wait = music_generator_new.run_midi(composer)
    return send_file('./output.midi', download_name='output.midi', mimetype='audio/midi')

if __name__ == "__main__":
    app.run()