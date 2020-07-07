package com.google.android.gms.samples.vision.ocrreader;

import android.graphics.Rect;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ObjectDetectionActivity extends AppCompatActivity {

    /*private class YourAnalyzer implements ImageAnalysis.Analyzer {

        @Override
        @androidx.camera.core.ExperimentalGetImage
        public void analyze(ImageProxy imageProxy) {

            Image mediaImage = imageProxy.getImage();
            if (mediaImage != null) {
                InputImage image =
                        InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
                // Pass image to an ML Kit Vision API
                // ...
                LocalModel localModel =
                        new LocalModel.Builder()
                                .setAssetFilePath("mobilenet_v1_1.0_128_quantized_1_default_1.tflite")
                                // or .setAbsoluteFilePath(absolute file path to tflite model)
                                .build();

                CustomObjectDetectorOptions customObjectDetectorOptions =
                        new CustomObjectDetectorOptions.Builder(localModel)
                                .setDetectorMode(CustomObjectDetectorOptions.SINGLE_IMAGE_MODE)
                                .enableMultipleObjects()
                                .enableClassification()
                                .setClassificationConfidenceThreshold(0.5f)
                                .setMaxPerObjectLabelCount(3)
                                .build();

                ObjectDetector objectDetector =
                        ObjectDetection.getClient(customObjectDetectorOptions);

                objectDetector
                        .process(image)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText(getApplicationContext(), "Fail. Sad!", Toast.LENGTH_SHORT).show();
                                //textView.setText("Fail. Sad!");
                                imageProxy.close();
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<List<DetectedObject>>() {
                            @Override
                            public void onSuccess(List<DetectedObject> results) {

                                for (DetectedObject detectedObject : results) {
                                    Rect box = detectedObject.getBoundingBox();


                                    for (DetectedObject.Label label : detectedObject.getLabels()) {
                                        String text = label.getText();
                                        int index = label.getIndex();
                                        float confidence = label.getConfidence();
                                        textView.setText(text);



                                    }}
                                imageProxy.close();
                            }
                        });

            }
            //ImageAnalysis.Builder.fromConfig(new ImageAnalysisConfig).setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST);

        }

    }


    PreviewView prevView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    TextView textView;

    private int REQUEST_CODE_PERMISSI
    ONS = 101;
    private String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA"};

*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_detection);
    }
}
