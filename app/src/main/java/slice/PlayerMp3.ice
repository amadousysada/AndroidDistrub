module PlayerMp3 {

  class Son{
    string name;
    string singer;
    string author;
    string album;
    string path;
  };


  sequence <Son> SonList;
  sequence<string> StringSeq;

  interface Player {

    bool addSon(string name,string singer,string author,string album,string path);
    bool deleteSon(string name);
    Son searchSon(string name);
    SonList getSons();
    string MafactoryMethode(StringSeq s );
    Son getCurrentSon();
    bool play(string name);
    void stop();
    void pause();
    string reprendre();
   };

};
