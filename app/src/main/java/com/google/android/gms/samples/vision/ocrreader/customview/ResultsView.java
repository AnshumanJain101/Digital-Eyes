package com.google.android.gms.samples.vision.ocrreader.customview;

import java.util.List;
import com.google.android.gms.samples.vision.ocrreader.tflite.Classifier.Recognition;

public interface ResultsView {
    public void setResults(final List<Recognition> results);
}
