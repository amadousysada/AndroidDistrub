module TP {

    class son {

            string album;
            string genre;
            string title;
            string date;
            string author;
            string path;
            int id;

    };
    sequence <son> sons;
    interface Morceau {

        void ajouter(son s);
        void supprimer(son s);
        son findByTitle(string name);
        sons findByAlbum(string name);
        sons findByAuthor(string name);
        sons findByGenre(string genre);
        son findById(int id);


    };

};