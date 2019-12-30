package projet.cpoo;

import java.awt.Desktop;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.text.Element;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class main extends Application {
    VBox View = new VBox();
    JFXTextField txt = new JFXTextField();

    Gestionnaire mon_gestionnaire = new Gestionnaire();



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        //BTN Telechargement
        JFXButton jfoenixButton = new JFXButton("TELECHARGER");
        jfoenixButton.setStyle("-fx-background-color: #95a5a6;");
        jfoenixButton.setOnAction(start_tele);
        //TEXTFIELD

        txt.setStyle("-fx-background-color: white;");
        txt.setPrefWidth(400);
        txt.setPromptText("Introduire le lien ici ");
        txt.setPadding(new Insets(5));
        txt.setFocusColor(Color.TRANSPARENT);


       //Main_vue

        VBox main_vue= new VBox();
        main_vue.setStyle("-fx-background-color: white;");

        // UP_Bare
        HBox Up_bare= new HBox();
        HBox.setMargin(txt, new Insets(0, 0, 0, 200));
        HBox.setMargin(jfoenixButton, new Insets(0, 0, 0, 20));

        Up_bare.getChildren().add(txt);
        Up_bare.getChildren().add(jfoenixButton);
        Up_bare.setStyle("-fx-background-color: #34495e;");
        Up_bare.prefWidthProperty().bind(main_vue.widthProperty());
        Up_bare.setMinHeight(50);
        Up_bare.setAlignment(Pos.CENTER_LEFT);

        // ----------------------- Menu_bare--------------------------
        VBox Menu_bare = new VBox();
        Menu_bare.setStyle("-fx-background-color: #2f3640;"+
                "-fx-border-width: 0 1 0 0;"+
                "-fx-border-color: grey;");
        Menu_bare.setMinWidth(200);
        Menu_bare.setPrefHeight(700);
                 //  --------------comp----------


        Menu_bare.getChildren().addAll(Menu_Item("icons/play.png",20,"EN COURS",startevent),
                Menu_Item("icons/pause-circular-button.png",20,"EN PAUSE",pauseevent),
                Menu_Item("icons/delete-button.png",20,"ANNULES",annuleevent),
                Menu_Item("icons/checked.png",20,"TERMINES",termineevent)
                );

       //the view
        View.setStyle("-fx-background-color: #ecf0f1;");
        View.prefWidthProperty().bind(main_vue.widthProperty());
        ScrollPane pane = new ScrollPane();
        pane.setContent(View);
        View.setMaxWidth(580);

        //Body
        HBox body =new HBox();
        body.getChildren().addAll(Menu_bare,pane);







        // add comp to main vue
        main_vue.getChildren().add(Up_bare);
        main_vue.getChildren().add(body);



        Scene scene = new Scene(main_vue, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }



    ImageView make_image(String path , double size)
    {
        Image s = new Image(getClass().getClassLoader().getResourceAsStream(path));
        ImageView view= new ImageView(s);
        view.setFitHeight(size);
        view.setPreserveRatio(true);
        return  view;
    }

    //     -------------  Liste des telechargement en cours  ------------------

    EventHandler<ActionEvent> startevent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            View.getChildren().clear();

            mon_gestionnaire.TelechEnCour().forEach( elem -> {


                VBox v=C_P_card(elem.getUrl(),pause_tele,annul_tele,
                        "icons/pause-circular-button.png","icons/delete-button.png",elem);
                VBox.setMargin(v,new Insets(10));
                View.getChildren().add(v);


            });


        }
    };

    // ----------------------- Liste des telechargement en pause -------------------

    EventHandler<ActionEvent> pauseevent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {

            View.getChildren().clear();
            mon_gestionnaire.TelechEnPause().forEach( elem -> {


                VBox v=C_P_card(elem.getUrl(),resume_tele,annul_tele,
                        "icons/play.png","icons/delete-button.png",elem);
                VBox.setMargin(v,new Insets(10));
                View.getChildren().add(v);


            });

        }
    };
    //  --------------------  Liste des telechargements annulées --------------------

    EventHandler<ActionEvent> annuleevent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {


            View.getChildren().clear();
            mon_gestionnaire.TelechAnnul().forEach( elem -> {


                VBox v=A_L_card("htttp",start_tele_again,
                        "icons/continuous.png",elem);
                VBox.setMargin(v,new Insets(10));
                View.getChildren().add(v);


            });

        }
    };
    // -------------------  Liste des telechargements terminées  ----------------------

    EventHandler<ActionEvent> termineevent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {

            View.getChildren().clear();
            mon_gestionnaire.TelechTermin().forEach( elem -> {


                VBox v=A_L_card(elem.getUrl(),browse_tele,
                        "icons/icon.png",elem);
                VBox.setMargin(v,new Insets(10));
                View.getChildren().add(v);


            });



        }
    };


    //------------------- Click pour lancer le telechargement -------------------

    EventHandler<ActionEvent> start_tele = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {

            try {
                mon_gestionnaire.telecharger(new URL(txt.getText()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    };
    EventHandler<ActionEvent> start_tele_again = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {

            try {
                mon_gestionnaire.telecharger(new URL(((Tele_button)e.getSource()).fichier.getUrl()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    };

    //------------------ Click pour mise en pause de telechargement ----------------
    EventHandler<ActionEvent> pause_tele = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {

            mon_gestionnaire.pause(((Tele_button)e.getSource()).fichier);

        }
    };

    //------------------- click pour reprendre le telechargement -------------------
    EventHandler<ActionEvent> resume_tele = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            mon_gestionnaire.resume(((Tele_button)e.getSource()).fichier);

        }
    };

    //------------------ click pour annuler le telechargement  ---------------------
    EventHandler<ActionEvent> annul_tele = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {

            mon_gestionnaire.cancel(((Tele_button)e.getSource()).fichier);

        }
    };

    //------------------ click pour ouvrir le dossier --------------------
    EventHandler<ActionEvent> browse_tele = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
           /* try {
                Desktop.getDesktop().open(new File(((Tele_button)e.getSource()).fichier.getDossier()));


            }catch (Exception s){
                s.printStackTrace();
            }*/
        }
    };






    JFXButton Menu_Item(String path , double size,String text, EventHandler<ActionEvent> event)
    {
        HBox item = new HBox();
        item.setAlignment(Pos.CENTER_LEFT);
        ImageView a=make_image(path,size);
        Label l=new Label(text);
        l.setFont(new Font("Arial",15));
        l.setTextFill(Color.web("#7f8c8d"));

        HBox.setMargin(a,new Insets(0,20,0,5));
        HBox.setMargin(l,new Insets(0,10,0,5));

        item.getChildren().addAll(a,l);
        item.setPadding(new Insets(10,5,5,5));


        JFXButton b= new JFXButton("",item);
        b.setMinWidth(200);
        b.setOnAction(event);
        b.setStyle("-fx-border-color:grey;"+
        "-fx-border-width: 0 0 0.5 0;");
        return b;
    }

    VBox C_P_card(String link ,EventHandler<ActionEvent> event_pause,EventHandler<ActionEvent> event_stop ,String one,String two,Telechargement fichier)
    {   //Main_card
        VBox b = new VBox();
        // LINK ICON + TEXT
        HBox l_t=new HBox();
        l_t.setAlignment(Pos.CENTER_LEFT);
        ImageView _link=make_image("icons/link(1).png",30);
        Text text=new Text(link);
        text.setWrappingWidth(400);
        HBox.setMargin(_link,new Insets(20,0,20,20));
        HBox.setMargin(text,new Insets(20,20,20,50));
        l_t.getChildren().addAll(_link,text);
        l_t.prefWidthProperty().bind(b.widthProperty());

        // pause + stop

        HBox p_s= new HBox();
        p_s.setAlignment(Pos.CENTER_RIGHT);

        Tele_button pause=new Tele_button("",make_image(one,20));
        pause.fichier=fichier;
        pause.setOnAction(event_pause);
        HBox.setMargin(pause,new Insets(0,1,3,0));

        Tele_button stop = new Tele_button("",make_image(two,20));
        pause.fichier=fichier;
        stop.setOnAction(event_stop);
        HBox.setMargin(stop,new Insets(0,20,3,0));
        p_s.getChildren().addAll(pause,stop);


        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(4.0);
        dropShadow.setRadius(20);
        b.getChildren().addAll(l_t,p_s);
        b.setStyle("-fx-background-color: #ffffff;"+
                   "-fx-background-radius: 5;");
        b.setEffect(dropShadow);





        return b;

    }



    VBox A_L_card(String link ,EventHandler<ActionEvent> event_start,String icon,Telechargement ficher)
    {   //Main_card
        VBox b = new VBox();
        // LINK ICON + TEXT
        HBox l_t=new HBox();
        l_t.setAlignment(Pos.CENTER_LEFT);
        ImageView _link=make_image("icons/link(1).png",30);
        Text text=new Text(link);
        text.setWrappingWidth(400);
        HBox.setMargin(_link,new Insets(20,0,20,20));
        HBox.setMargin(text,new Insets(20,20,20,50));
        l_t.getChildren().addAll(_link,text);
        l_t.prefWidthProperty().bind(b.widthProperty());

        // pause + stop

        HBox p_s= new HBox();
        p_s.setAlignment(Pos.CENTER_RIGHT);


        //------------ Start button
        Tele_button start = new Tele_button("",make_image(icon,20));
        start.fichier=ficher;
        start.setOnAction(event_start);
        HBox.setMargin(start,new Insets(0,20,3,0));
        p_s.getChildren().addAll(start);


        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(4.0);
        dropShadow.setRadius(20);
        b.getChildren().addAll(l_t,p_s);
        b.setStyle("-fx-background-color: #ffffff;"+
                "-fx-background-radius: 5;");
        b.setEffect(dropShadow);

        return b;

    }


    class Tele_button extends JFXButton {
        public Telechargement  fichier;

        public Tele_button(String text, Node graphic){ super(text,graphic);}
    }
}
