package sample;

import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class AboutController {
    public ImageView imageView;
    private final Image backImg;
    private final Image frontImg;
    public Hyperlink creditsLink;
    public Hyperlink backLink;
    public AnchorPane anchorPaneBack;

    public AboutController() {
        backImg = new Image(getClass().getResourceAsStream("/img/aboutBack.png"));
        frontImg = new Image(getClass().getResourceAsStream("/img/aboutFront.png"));
    }

    public void backAction() {
        imageView.setImage(frontImg);
        creditsLink.setOpacity(1);
        creditsLink.setDisable(false);
        backLink.setOpacity(0);
        backLink.setDisable(true);
        anchorPaneBack.setOpacity(0);
    }

    public void showCreditsAction() {
        imageView.setImage(backImg);
        creditsLink.setOpacity(0);
        creditsLink.setDisable(true);
        backLink.setOpacity(1);
        backLink.setDisable(false);
        anchorPaneBack.setOpacity(1);
    }
}