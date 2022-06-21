import pandas as pd
import numpy as np
import tensorflow as tf

#Recommended logger thingy
tf.get_logger().setLevel('ERROR')

#Load training and testing data
train_df = pd.read_csv(
    "data/train/is_train.csv",
    names=["Query", "Intent"])

test_df = pd.read_csv(
    "data/test/is_test.csv",
    names=["Query", "Intent"])

#Extract training data features and intents
train_features = train_df.copy()
train_intents = train_features.pop('Intent')
train_classes = np.sort(train_intents.unique(), kind='quicksort')
num_classes_train = len(train_classes)
train_intents_OHE = pd.get_dummies(train_intents)

#Extract testing data features and intents
test_features = test_df.copy()
test_intents = test_features.pop('Intent')
test_classes = np.sort(test_intents.unique(), kind='quicksort')
num_classes_test = len(test_classes)
test_intents_OHE = pd.get_dummies(test_intents)

#Setup encoder for text vectorization
VOCAB_SIZE = 10000
encoder = tf.keras.layers.experimental.preprocessing.TextVectorization(
    max_tokens=VOCAB_SIZE)
encoder.adapt(train_features.to_numpy())
vocab = np.array(encoder.get_vocabulary())
vocab[:20]

#Setup model to use encoder layer, bidirectional LSTM, and 2 Dense layers.
model = tf.keras.Sequential([
    encoder,
    tf.keras.layers.Embedding(
        input_dim=len(encoder.get_vocabulary()),
        output_dim=64,
        mask_zero=True),
    tf.keras.layers.Bidirectional(tf.keras.layers.LSTM(64)),
    tf.keras.layers.Dense(64, activation='relu'),
    tf.keras.layers.Dense(num_classes_train, activation='softmax')
])

#Compile model and produce accuracy metric
model.compile(loss=tf.keras.losses.CategoricalCrossentropy(),
              optimizer=tf.keras.optimizers.Adam(1e-4),
              metrics=['accuracy'])

#Fit model to training data and evaluate using testing data
history = model.fit(x=train_features,y=train_intents_OHE,epochs=50)

#Save model along with classes to directory
model.save("query-intent-model")

out_file = open(r"query_intent_classes.csv","w")
for i in range(0,len(train_classes)+1):
    out_file.write(train_classes[i-1]+"\n")