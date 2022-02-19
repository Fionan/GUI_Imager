/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui_ocr;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author fionan Obrien
 *
 * This is a helper program to help locate specific frames
 *
 */
public class GUI_Imager extends Application {

    static final String TITLE = "GUI_IMGER";
    static final File Default_File = null;
    static final Point Default_Point = new Point(0, 0);
    static final String Default_activePoint = "P1";

    //set to defaults
    static Point p1 = Default_Point;
    static Point p2 = Default_Point;
    static File selectedFile = Default_File;
    static int fileSelectNum = 0;
    static String activePoint = Default_activePoint;

    static Button fileChooseButton,
            BWFilterButton,
            GridFilterButton,
            CropFilterButton,
            pointToggleButton,
            censorFilterButton,
            clearButton,
            nextButton,
            previousButton,
            saveButton;

    static Label info_label,
            slider_label;

    static Slider slider;
    static ImageView imageView;
    static ArrayList<Image> activeImages = new ArrayList();

    static BufferedImage activeBufferedImage;

    static File argumentFile = null;

    private static String lastVisitedDirectory = System.getProperty("user.home");

    static ArrayList<File> filesSelected = null;

    @Override
    public void start(Stage primaryStage) {

        info_label = new Label();
        slider_label = new Label();

        fileChooseButton = new Button();
        imageView = new ImageView();
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                double x = event.getX();
                double y = event.getY();

                //  System.out.println(event.getX()+" "+event.getY());
                if ("P1".equals(activePoint)) {
                    p1 = new Point((int) x, (int) y);
                } else {
                    p2 = new Point((int) x, (int) y);

                    p2 = new Point(p2.x - p1.x, p2.y - p1.y);

                }

                String message = "Starting Point: " + p1.x + " " + p1.y + " | End point " + p2.x + " " + p2.y;

                info_label.setText(message);
                event.consume();
            }
        });

        fileChooseButton.setText("Select File");
        fileChooseButton.setOnAction((ActionEvent event) -> {

            FileChooser fileChooser = new FileChooser();
            if (argumentFile != null) {
                fileChooser.setInitialDirectory(argumentFile);
            }
            List filesSelected = fileChooser.showOpenMultipleDialog(primaryStage);

            if (filesSelected != null) {

                processSelectedFiles(filesSelected);

            }
        });

        BWFilterButton = new Button();
        BWFilterButton.setText("B & W Filter");
        BWFilterButton.setOnAction((ActionEvent event) -> {

            if (selectedFile != null) {
                activeBufferedImage = new BlackWhiteFilter(activeBufferedImage).apply();
                Image image = SwingFXUtils.toFXImage(activeBufferedImage, null);
                imageView.setImage(image);
            }

        }
        );

        GridFilterButton = new Button();
        GridFilterButton.setText("Grid Filter");
        GridFilterButton.setOnAction((ActionEvent event) -> {

            if (selectedFile != null) {
                activeBufferedImage = new GridFilter(activeBufferedImage).apply();
                Image image = SwingFXUtils.toFXImage(activeBufferedImage, null);
                imageView.setImage(image);
            }

        }
        );

        CropFilterButton = new Button();
        CropFilterButton.setText("Crop Filter");
        CropFilterButton.setOnAction((ActionEvent event) -> {

            if (selectedFile != null) {
                activeBufferedImage = new CropFilter(p1, p2, activeBufferedImage).apply();
                Image image = SwingFXUtils.toFXImage(activeBufferedImage, null);
                imageView.setImage(image);
            }

        }
        );
        censorFilterButton = new Button();
        censorFilterButton.setText("Censor Filter");
        censorFilterButton.setOnAction((ActionEvent event) -> {

            if (selectedFile != null) {
                activeBufferedImage = new CensorFilter(p1, p2, activeBufferedImage).apply();
                Image image = SwingFXUtils.toFXImage(activeBufferedImage, null);
                imageView.setImage(image);
            }

        }
        );

        pointToggleButton = new Button();
        pointToggleButton.setText("Point Toggle");
        pointToggleButton.setOnAction((ActionEvent event) -> {

            if ("P1".equals(activePoint)) {
                activePoint = "P2";
            } else {
                activePoint = "P1";

            }

        }
        );
        clearButton = new Button();
        clearButton.setText("Clear");
        clearButton.setOnAction((ActionEvent event) -> {

            imageView.setImage(null);
            p1 = Default_Point;
            p2 = Default_Point;
            selectedFile = Default_File;
            activePoint = Default_activePoint;
            update();

        }
        );

        nextButton = new Button();
        nextButton.setText("NEXT");
        nextButton.setOnAction((ActionEvent event) -> {

            if (activeImages.size() > 0) {

                //file selectcounter
                if (fileSelectNum < activeImages.size()) {
                    fileSelectNum++;
                } else {
                    fileSelectNum = activeImages.size();

                }
                Image i = imageView.getImage();
                int imageIndex = activeImages.indexOf(i);
                if (imageIndex < activeImages.size()) {
                    imageView.setImage(activeImages.get(imageIndex + 1));
                }

            }

        }
        );

        previousButton = new Button();
        previousButton.setText("Previous");
        previousButton.setOnAction((ActionEvent event) -> {

            if (activeImages.size() > 0) {

                //file selectcounter
                if (fileSelectNum > 0) {
                    fileSelectNum--;
                } else {

                    fileSelectNum = 0;
                }

                Image i = imageView.getImage();
                int imageIndex = activeImages.indexOf(i);
                if (imageIndex > 0) {
                    imageView.setImage(activeImages.get(imageIndex - 1));
                }

            }

        }
        );

        saveButton = new Button();
        saveButton.setText("Save Image");
        saveButton.setOnAction((ActionEvent event) -> {
            try {

                String newFileName = selectedFile.getParentFile() + "\\" + selectedFile.getName().substring(0, selectedFile.getName().indexOf("."));

                File output = new File(newFileName + "_EDITED.png");
                ImageIO.write(activeBufferedImage, "png", output);

            } catch (IOException e) {
            }
        });

        slider = new Slider();
        slider.setPrefWidth(800);
        slider.setMajorTickUnit(10);
        slider.setMajorTickUnit(1);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                imageView.setImage(activeImages.get(newValue.intValue()));
                slider_label.setText(newValue.intValue() + "");

            }

        });

        Region bottomFiller = new Region();

        //    FlowPane root = new FlowPane(); 
        VBox root = new VBox();

        HBox toolsMenu = new HBox(25);
        HBox bottomMenu = new HBox(25);
        HBox infoMenu = new HBox(25);
        HBox fileMenu = new HBox(1);
        ScrollPane scrollPane = new ScrollPane();

        scrollPane.pannableProperty()
                .set(true);
        scrollPane.setContent(imageView);

        scrollPane.hbarPolicyProperty()
                .setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.vbarPolicyProperty()
                .setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        root.getChildren()
                .add(fileMenu);
        root.getChildren()
                .add(infoMenu);
        root.getChildren()
                .add(scrollPane);
        root.getChildren()
                .add(bottomFiller);
        root.getChildren()
                .add(toolsMenu);
        root.getChildren()
                .add(bottomMenu);

        toolsMenu.getChildren()
                .add(pointToggleButton);
        toolsMenu.getChildren()
                .add(clearButton);
        toolsMenu.getChildren()
                .add(previousButton);
        toolsMenu.getChildren()
                .add(nextButton);
        toolsMenu.getChildren()
                .add(slider);

        infoMenu.getChildren()
                .add(info_label);
        infoMenu.getChildren()
                .add(slider_label);

        fileMenu.getChildren()
                .add(fileChooseButton);
        bottomMenu.getChildren()
                .add(BWFilterButton);
        bottomMenu.getChildren()
                .add(GridFilterButton);
        bottomMenu.getChildren()
                .add(CropFilterButton);
        bottomMenu.getChildren()
                .add(saveButton);
        bottomMenu.getChildren()
                .add(censorFilterButton);

        Scene scene = new Scene(root, 1200, 800);

        primaryStage.setTitle(TITLE);

        primaryStage.setScene(scene);

        update();

        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        locateWorkingDirectoryFromArgs(args);
        launch(args);
    }

    public static void update() {

        if (activeImages.isEmpty()) {
            BWFilterButton.setDisable(true);
            GridFilterButton.setDisable(true);
            CropFilterButton.setDisable(true);
            pointToggleButton.setDisable(true);
            clearButton.setDisable(true);
            previousButton.setDisable(true);
            nextButton.setDisable(true);
            saveButton.setDisable(true);
            slider.setDisable(true);
        } else {
            BWFilterButton.setDisable(false);
            GridFilterButton.setDisable(false);
            CropFilterButton.setDisable(false);
            pointToggleButton.setDisable(false);
            clearButton.setDisable(false);
            previousButton.setDisable(false);
            nextButton.setDisable(false);
            saveButton.setDisable(false);
            slider.setDisable(false);
        }

        if (activeImages.size() > 1) {
            slider.setMin(0);
            slider.setMax((double) activeImages.size());
            slider.setValue(0);
            slider.setShowTickLabels(true);

            slider.setDisable(false);

        } else {
            slider.setShowTickLabels(false);
            slider.setDisable(true);
        }

    }

    static void importImages() {
        File[] files = selectedFile.listFiles();
        String monthcode = selectedFile.getName().substring(2);

        for (File f : files) {
            if (f.getName().substring(2).equalsIgnoreCase(monthcode)) {

                Image img = new Image(f.toURI().toString());
                activeImages.add(img);
            }
        }

        if (activeBufferedImage == null) {
            activeBufferedImage = SwingFXUtils.fromFXImage(activeImages.get(0), null);
            imageView.setImage(activeImages.get(0));
            update();

        }
    }

    /*
    java -jar pdfbox-app-2.0.18.jar PDFToImage -dpi 360 a.pdf
     */
    private void processSelectedFiles(List<File> filesSelected) {

        activeImages = new ArrayList<Image>();

        for (File f : filesSelected) {

            String fileType = f.getName();
            fileType = fileType.substring(fileType.lastIndexOf("."));
            fileType = fileType.toLowerCase();

            switch (fileType) {
                case ".jpg":
                case ".png":
                    Image img = new Image(f.toURI().toString());
                    activeImages.add(img);
                    break;

            }
            selectedFile = filesSelected.get(fileSelectNum);
        }

        activeBufferedImage = SwingFXUtils.fromFXImage(activeImages.get(0), null);
        imageView.setImage(activeImages.get(0));
        update();
    }

    static void locateWorkingDirectoryFromArgs(String args[]) {

        if (args != null) {

            if (args.length > 0) {

                System.out.println("Args content is " + args[0]);

                File f = new File(args[0]).getParentFile();
                argumentFile = f;
            }

        }

    }
}
