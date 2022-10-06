import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Funktional {
    public static void main(String[] args) throws IOException {

        byte[] file_contents = Files.readAllBytes(Paths.get("alben.xml"));

        List<String> tokenList = new ArrayList<>();
        Funktional token = new Funktional();
        ArrayList<Album> albums = new ArrayList<Album>();

        tokenList = token.createTokenList(file_contents, 0, tokenList);
        parseFile(albums, tokenList, 0);
        printAlbums(albums);
    }

    public List<String> createTokenList(byte[] file_contents, int current_character, List<String> tokenList) {

        if (file_contents[current_character] == '\n' || file_contents[current_character] == '\r' || file_contents[current_character] == '\t') {
            createTokenList(file_contents, current_character += 1, tokenList);
        } else if (new String(file_contents, current_character, 7, StandardCharsets.UTF_8).equals(new String("<album>"))) {
            tokenList.add("album");
            createTokenList(file_contents, current_character += 7, tokenList);
        } else if (new String(file_contents, current_character, 8, StandardCharsets.UTF_8).equals(new String("</album>"))) {
            tokenList.add("/album");

            //Only if end of xml is not reached yet
            if (file_contents.length > current_character + 8)
                createTokenList(file_contents, current_character += 8, tokenList);
            else
                return tokenList;
        } else if (new String(file_contents, current_character, 7, StandardCharsets.UTF_8).equals(new String("<title>"))) {
            tokenList.add("title");
            current_character += 7;

            int indexOfNextToken = indexOfChar(file_contents, '<', current_character);
            tokenList.add(new String(file_contents, current_character, indexOfNextToken - current_character, StandardCharsets.UTF_8));

            tokenList.add("/title");
            current_character += indexOfNextToken - current_character + 8;
            createTokenList(file_contents, current_character, tokenList);
        } else if (new String(file_contents, current_character, 8, StandardCharsets.UTF_8).equals(new String("<artist>"))) {
            tokenList.add("artist");
            current_character += 8;

            int indexOfNextToken = indexOfChar(file_contents, '<', current_character);
            tokenList.add(new String(file_contents, current_character, indexOfNextToken - current_character, StandardCharsets.UTF_8));

            tokenList.add("/artist");
            current_character += indexOfNextToken - current_character + 9;
            createTokenList(file_contents, current_character, tokenList);
        } else if (new String(file_contents, current_character, 8, StandardCharsets.UTF_8).equals(new String("<rating>"))) {
            tokenList.add("rating");
            current_character += 8;

            int indexOfNextToken = indexOfChar(file_contents, '<', current_character);
            tokenList.add(new String(file_contents, current_character, indexOfNextToken - current_character, StandardCharsets.UTF_8));

            tokenList.add("/rating");
            current_character += indexOfNextToken - current_character + 9;
            createTokenList(file_contents, current_character, tokenList);
        } else if (new String(file_contents, current_character, 7, StandardCharsets.UTF_8).equals(new String("<track>"))) {
            tokenList.add("track");
            createTokenList(file_contents, current_character += 8, tokenList);
        } else if (new String(file_contents, current_character, 8, StandardCharsets.UTF_8).equals(new String("</track>"))) {
            tokenList.add("/track");
            createTokenList(file_contents, current_character += 9, tokenList);
        } else if (new String(file_contents, current_character, 9, StandardCharsets.UTF_8).equals(new String("<feature>"))) {
            tokenList.add("feature");
            current_character += 9;

            int indexOfNextToken = indexOfChar(file_contents, '<', current_character);
            tokenList.add(new String(file_contents, current_character, indexOfNextToken - current_character, StandardCharsets.UTF_8));

            tokenList.add("/feature");
            current_character += indexOfNextToken - current_character + 10;
            createTokenList(file_contents, current_character, tokenList);
        } else if (new String(file_contents, current_character, 8, StandardCharsets.UTF_8).equals(new String("<length>"))) {
            tokenList.add("length");
            current_character += 8;

            int indexOfNextToken = indexOfChar(file_contents, '<', current_character);
            tokenList.add(new String(file_contents, current_character, indexOfNextToken - current_character, StandardCharsets.UTF_8));

            tokenList.add("/length");
            current_character += indexOfNextToken - current_character + 9;
            createTokenList(file_contents, current_character, tokenList);
        } else if (new String(file_contents, current_character, 9, StandardCharsets.UTF_8).equals(new String("<writing>"))) {
            tokenList.add("writing");
            current_character += 9;

            int indexOfNextToken = indexOfChar(file_contents, '<', current_character);
            tokenList.add(new String(file_contents, current_character, indexOfNextToken - current_character, StandardCharsets.UTF_8));

            tokenList.add("/writing");
            current_character += indexOfNextToken - current_character + 10;
            createTokenList(file_contents, current_character, tokenList);
        } else if (new String(file_contents, current_character, 6, StandardCharsets.UTF_8).equals(new String("<date>"))) {
            tokenList.add("date");
            current_character += 6;

            int indexOfNextToken = indexOfChar(file_contents, '<', current_character);
            tokenList.add(new String(file_contents, current_character, indexOfNextToken - current_character, StandardCharsets.UTF_8));

            tokenList.add("/date");
            current_character += indexOfNextToken - current_character + 7;
            createTokenList(file_contents, current_character, tokenList);
        }
        return tokenList;
    }

    public int indexOfChar(byte[] haystack, char needle, int current_character) {
        return haystack[current_character] == needle ? current_character : indexOfChar(haystack, needle, current_character += 1);
    }

    public static void parseFile(ArrayList<Album> albums, List<String> tokenList, int key) {

        if (tokenList.get(key).equals("album")) {
            int current_album = albums.size();
            albums.add(new Album());
            parseAlbum(albums, current_album, tokenList, key += 1);
        }
    }

    public static void parseAlbum(ArrayList<Album> albums, int current_album, List<String> tokenList, int key) {


        if (tokenList.get(key).equals("title")) {
            albums.get(current_album).title = tokenList.get(key += 1);
            parseAlbum(albums, current_album, tokenList, key += 2);
        } else if (tokenList.get(key).equals("artist")) {
            albums.get(current_album).artist = tokenList.get(key += 1);
            parseAlbum(albums, current_album, tokenList, key += 2);
        } else if (tokenList.get(key).equals("date")) {
            albums.get(current_album).date = tokenList.get(key += 1);
            parseAlbum(albums, current_album, tokenList, key += 2);
        } else if (tokenList.get(key).equals("track")) {
            int current_track = albums.get(current_album).tracks.size();
            albums.get(current_album).tracks.add(new Track());
            parseTrack(albums, current_album, current_track, tokenList, key += 1);
        } else if (tokenList.get(key).equals("/album") && tokenList.size() - 1 != key) {
            parseFile(albums, tokenList, key += 1);
        }
    }

    public static void parseTrack(ArrayList<Album> albums, int current_album, int current_track, List<String> tokenList, int key) {

        if (tokenList.get(key).equals("writing")) {
            albums.get(current_album).tracks.get(current_track).writers.add(tokenList.get(key += 1));
            parseTrack(albums, current_album, current_track, tokenList, key += 2);
        } else if (tokenList.get(key).equals("feature")) {
            albums.get(current_album).tracks.get(current_track).features.add(tokenList.get(key += 1));
            parseTrack(albums, current_album, current_track, tokenList, key += 2);
        } else if (tokenList.get(key).equals("title")) {
            albums.get(current_album).tracks.get(current_track).title = tokenList.get(key += 1);
            parseTrack(albums, current_album, current_track, tokenList, key += 2);
        } else if (tokenList.get(key).equals("length")) {
            albums.get(current_album).tracks.get(current_track).length = tokenList.get(key += 1);
            parseTrack(albums, current_album, current_track, tokenList, key += 2);
        } else if (tokenList.get(key).equals("rating")) {
            albums.get(current_album).tracks.get(current_track).rating = Integer.parseInt(tokenList.get(key += 1));
            parseTrack(albums, current_album, current_track, tokenList, key += 2);
        } else if (tokenList.get(key).equals("/track")) {
            parseAlbum(albums, current_album, tokenList, key += 1);
        }

    }

    public static void printAlbums(ArrayList<Album> albums) {
        for (int i = 0; i < albums.size(); i++) {
            System.out.println(albums.get(i));
        }
    }

}

