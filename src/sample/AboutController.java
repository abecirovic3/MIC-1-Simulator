package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AboutController {
    public ImageView imageView;
    private final Image backImg;
    private final Image frontImg;
    public Hyperlink creditsLink;
    public Hyperlink backLink;
    public Label contentLabel;
    public Label creditsLabel;

    public AboutController() {
        backImg = new Image(getClass().getResourceAsStream("/img/aboutBack.png"));
        frontImg = new Image(getClass().getResourceAsStream("/img/aboutFront.png"));
    }

    public void backAction() {
        imageView.setImage(frontImg);
        creditsLink.setOpacity(1);
        creditsLink.setDisable(false);
        contentLabel.setOpacity(0);
        backLink.setOpacity(0);
        backLink.setDisable(true);
        creditsLabel.setOpacity(0);
    }

    public void showCreditsAction() {
        imageView.setImage(backImg);
        creditsLink.setOpacity(0);
        creditsLink.setDisable(true);
        contentLabel.setOpacity(1);
        backLink.setOpacity(1);
        backLink.setDisable(false);
        creditsLabel.setOpacity(1);
    }
}
