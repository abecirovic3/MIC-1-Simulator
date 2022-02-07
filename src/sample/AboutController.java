package sample;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AboutController {
    public ImageView imageView;
    public Button frontBtn;
    public Button backBtn;
    private final Image backImg;
    private final Image frontImg;

    public AboutController() {
        backImg = new Image(getClass().getResourceAsStream("/img/aboutBack.png"));
        frontImg = new Image(getClass().getResourceAsStream("/img/aboutFront.png"));
    }

    public void flipToBack() {
        imageView.setImage(backImg);
        frontBtn.setDisable(true);
        backBtn.setDisable(false);
    }

    public void flipToFront() {
        imageView.setImage(frontImg);
        frontBtn.setDisable(false);
        backBtn.setDisable(true);
    }
}
