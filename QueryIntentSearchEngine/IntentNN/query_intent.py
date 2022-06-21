import sys
import pandas as pd
import numpy as np

#Read in user query
query = sys.argv[0]

#Load deep learning model and classes
model = tf.keras.models.load_model('query-intent-model')
model_classes = pd.read_csv(
    "quert_intent_classes.csv",
    names=["Intent"]).to_numpy()

#Run prediction and print output
pred = model.predict([query])[0]
maxIndex = np.argmax(pred)
print(model_classes[maxIndex])