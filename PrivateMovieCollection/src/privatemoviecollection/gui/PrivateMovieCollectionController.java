package privatemoviecollection.gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import privatemoviecollection.be.Category;
import privatemoviecollection.be.Movie;
import privatemoviecollection.bll.PMCLogic;

/**
 *
 * @author DKE
 */
public class PrivateMovieCollectionController implements Initializable{
    private ObservableList listMovie = FXCollections.observableArrayList();
    private ObservableList listCategory = FXCollections.observableArrayList();
    @FXML
    private Button searchBtn;
    @FXML
    private ListView<Movie> movies;
    @FXML
    private ListView<Category> categories;
    private PMCLogic bll;
    @FXML
    private TextField searchFld;
    
    public PrivateMovieCollectionController(){
        bll = new PMCLogic();
    }
    
    @FXML
    private void handleAddCategoryButton(ActionEvent e) throws IOException{
        Stage newCategoryWindow = new Stage();
        newCategoryWindow.initModality(Modality.NONE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewCategory.fxml"));
        Parent root = loader.load();
        NewCategoryController ncc = loader.getController();
        ncc.setParentWindowController(this);
        
        Scene scene = new Scene(root);
        newCategoryWindow.setTitle("New Category");
        newCategoryWindow.setScene(scene);
        newCategoryWindow.showAndWait();
    }
    
    public void addCategory(String name){
        try {
            Category category = bll.createCategory(name);
            categories.getItems().clear();
            listCategory.addAll(category);
            categories.getItems().addAll(listCategory);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void handleDeleteCategoryButton(ActionEvent e){
        try {
            Category selected =
                    categories.getSelectionModel().getSelectedItem();
            bll.deleteCategory(selected);
            categories.getItems().remove(selected);
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void handleAddEditMovieButton(ActionEvent e) throws IOException{
        openAddEditMovieWindow(e, null);
    }
    
    private void openAddEditMovieWindow(ActionEvent e, Movie selected) throws IOException{
        Stage addEditMovieWindow = new Stage();
        addEditMovieWindow.initModality(Modality.NONE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewEditMovie.fxml"));
        Parent root = loader.load();
        NewEditMovieController nmc = loader.getController();
        nmc.setParentWindowController(this);
        nmc.setMovieToEdit(selected);
        
        Scene scene = new Scene(root);
        addEditMovieWindow.setTitle("New / Edit Movie");
        addEditMovieWindow.setScene(scene);
        addEditMovieWindow.showAndWait();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        try {
            setImageSearch();
            
            //Add movie
            String a = "Title \t\t\t\t\t\t\t Rating \t\t\t\t\t Lastview";
            listMovie.add(a);
            List<Movie> listm = bll.getAllMovies();
            listMovie.addAll(listm);
            
            //Add category
            String b = "Categories";
            listCategory.add(b);
            List<Category> listc = bll.getAllCategories();
            listCategory.addAll(listc);
            
        }catch (MalformedURLException ex){
            ex.printStackTrace();
        }
    }
    
    public void addMovie(Movie m){
        try {
            Movie movie = bll.createMovie(m);
            movies.getItems().clear();
            listMovie.addAll(movie);
            movies.getItems().addAll(listMovie);
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void deleteMovie(ActionEvent e){
        try {
            Movie selected =
                    movies.getSelectionModel().getSelectedItem();
            bll.deleteMovie(selected);
            movies.getItems().remove(selected);
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    
    private void editMovie(ActionEvent e) throws IOException{
        Movie selected =
                movies.getSelectionModel().getSelectedItem();
        openAddEditMovieWindow(e, selected);
    }
    
    @FXML
    private void setImageSearch() throws MalformedURLException{
        Path dir = FileSystems.getDefault().getPath("./src/images/Search-icon.png");
        Image image = new Image(dir.toUri().toURL().toExternalForm());
        searchBtn.setGraphic(new ImageView(image));
    }
}