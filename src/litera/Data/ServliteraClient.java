package litera.Data;
/**
 * *
 *
 * @author Yigit Polat
 * <p>
 * <p>
 * IMPORTANT NOTE: A FILE IS DEFINED WITH A FILEID AND ITS FILENAME
 */

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ServliteraClient
{
    private final String SERVER_URL = "http://yigitpolat.com/";
    private String username;
    private String password;
    private String literaPath;

    private getThread getRequest;
    private postThread postRequest;

    private boolean auth = false;

    public ServliteraClient(String user, String pass, String path)
    {
        username = user;
        password = pass;
        literaPath = path;

        getRequest = new getThread();
        postRequest = new postThread();
    }

    public boolean initializeConnection()
    {
        try
        {
            // creates a HTTP connection
            URL url = new URL(SERVER_URL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.disconnect();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean login()
    {
        int responseCode;
        try
        {
            // creates a HTTP connection
            URL url = new URL(SERVER_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //Sets http request type
            con.setRequestMethod("POST");
            con.setRequestProperty("User", username);
            con.setRequestProperty("Pass", password);
            con.setRequestProperty("Type", "zero");

            responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                auth = true;
                return true;
            }
            else if (responseCode == 999)
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return false;
    }


    public String noteName(String fileID)
    {
        String[] fid = LocalDataManager.getFileIDs();
        String[] names = LocalDataManager.getNoteNames(literaPath);

        int index = 0;
        for (int i = 0; i < fid.length; i++)
        {
            if (fileID.equals(fid[i]))
                index = i;
        }

        return names[index];

    }

    public int createNewUser(String user, String pass, String email)
    {
        getRequest = new getThread();
        return getRequest.createUser(user, pass, email);
    }

    // 00, sound.wav
    // 00, image.jpg
    public String[] getHashesFromServlitera(String[] fids, String[] fnames)
    {
        String att1 = "";
        String att2 = "";

        for (int i = 0; i < fids.length; i++)
        {
            att1 += fids[i] + ",";
            att2 += fnames[i] + ",";
        }
        getRequest = new getThread();

        return getRequest.getHashes(att1, att2, fids.length);
    }

    //String[0][n] -> Fids
    //String[1][n] -> Filenames
    //String[2][n] -> hashes
    public String[][] filesAtServlitera()
    {
        String[] data;
        String[][] table;
        getRequest = new getThread();

        data = getRequest.fileData();

        if (data != null)
        {
            table = new String[3][data.length];
            for (int i = 0; i < data.length; i++)
            {
                int flag = 0;
                int separator = 0;
                for (int j = 0; j < data[i].length(); j++)
                {
                    if (data[i].charAt(j) == '%' || j == data[i].length() - 1) //extract file data separately
                    {
                        if (j != data[i].length() - 1)
                            table[separator++][i] = data[i].substring(0, j);
                        else
                            table[separator++][i] = data[i].substring(0, j + 1); //last char exception.

                        if (separator >= 3)
                            break;
                        data[i] = data[i].substring(j + 1, data[i].length());
                        j = 0;
                    }
                }
            }
            return table;
        }
        else
            return null;
    }

    public boolean bringFile(String fid, String fname)
    {
        getRequest = new getThread();
        getRequest.setFileID(fid);
        getRequest.setFileName(fname);

        getRequest.getFile();
        System.out.println("File receive task");
        return true;
    }

    public boolean sendFile(String fid, String fname)
    {
        postRequest = new postThread();
        postRequest.setPath(literaPath + "/" + noteName(fid) + "/" + fname);
        postRequest.setFileID(fid);

        postRequest.postFile();
        System.out.println("File send task");
        return true;
    }

    public void postNotes(String[] fileIDs)
    {
        String filepath;
        for (int i = 0; i < fileIDs.length; i++)
        {
            //get file path to upload from
            filepath = literaPath + "/" + noteName(fileIDs[i]);
            File f = new File(filepath);
            File[] files = f.listFiles();
            for (File fx : files)
            {
                postRequest = new postThread();
                postRequest.setPath(filepath + "/" + fx.getName());
                postRequest.setFileID(fileIDs[i]);
                postRequest.postFile();
            }
        }

    }

    public void getNotes(String[] fileIDs)
    {
        String filename, filepath;
        for (int i = 0; i < fileIDs.length; i++)
        {
            //define the file that is to be downloaded....
            getRequest.setFileID(fileIDs[i]);    //set the note where the file belongs
            filepath = literaPath + "/" + noteName(fileIDs[i]);
            File f = new File(filepath);
            File[] files = f.listFiles();
            for (File fx : files)
            {
                getRequest.setFileID(fileIDs[i]);
                getRequest.setFileName(fx.getName());   //define the file with its unique filename
                getRequest.setType(0); //set type to a get file request..
                getRequest.getFile();
            }
        }
    }

    public String generateHash(String fid, String fname)
    {
        try
        {
            String path = literaPath + "/" + noteName(fid) + "/" + fname; //set the location where the file is going to be read.

            File note = new File(path);
            FileInputStream fis = new FileInputStream(new File(path));
            String md5Hex = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis); //used the apache digestUtils package for generating a md5 hash code
            fis.close();

            return md5Hex;
        }
        catch (Exception e)
        {
            return "0";
        }
    }

    //
    //
    //INNER CLASSES ( my slaves)//
    //
    //
    //
    class getThread implements Runnable
    {
        Thread t;
        int rqType;
        String fileID, noteName, fileName;

        // 0 : get file
        // 1 : new user
        // 2 : get hashes
        public void setType(int t)
        {
            rqType = t;
        }

        public void setFileID(String fid)
        {
            fileID = fid;
            noteName = noteName(fid);
        }

        public void setFileName(String fname)
        {
            fileName = fname; //set the file's name we want to get from the Servlitera
        }

        public void run()
        {
            if (rqType == 0) //get file
            {
                getFile();
            }
        }

        public void start()
        {
            if (t == null)
            {
                t = new Thread(this, "get");
                t.start();
            }
        }

        public String[] getHashes(String a1, String a2, int n)
        {
            String[] hashes = new String[n];
            try
            {
                URL url = new URL(SERVER_URL);/* + "?"
                        + "User=" + username //username
                        + "&Pass=" + password //password
                        + "&Attributes1=" + a1 //FileIDs CSV
                        + "&Attributes2=" + a2 //filenames CSV
                        + "&Type=GET_HASHES"); //sets request type*/
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("POST");
                httpConn.setRequestProperty("User", username);
                httpConn.setRequestProperty("Pass", password);
                httpConn.setRequestProperty("Attributes1", a1);
                httpConn.setRequestProperty("Attributes2", a2);
                httpConn.setRequestProperty("Type", "GET_HASHES");


                InputStream inputStream = httpConn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream())); //READ HASHES IN THE SAME ORDER

                //READ HASHES LINE BY LINE
                String responseLine = reader.readLine();
                int i = 0;
                while (responseLine != null) //go through the file until end_of_file reached
                {
                    hashes[i] = responseLine;//add current line
                    i++; //update iteration
                    reader.readLine(); //next line please..
                }
                return hashes; //give me the hashes
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
                return null;
            }

        }

        public int createUser(String u, String p, String m)
        {
            try
            {
                // creates a HTTP connection
                URL url = new URL(SERVER_URL + "?"
                        + "Attributes1=" + u //username
                        + "&Attributes2=" + p //password
                        + "&Attributes3=" + m //email
                        + "&Type=NEW_USER"); //sets request type
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("GET");


                int serverStatus = httpConn.getResponseCode();

                httpConn.disconnect();
                if (serverStatus == 701)
                {

                    System.out.println("Server's response: " + serverStatus);
                    return 0;
                }
                else if (serverStatus == 700)
                {

                    System.out.println(" user data collision");
                    return 1;
                }
                else
                {
                    System.out.println("Server error:" + serverStatus);
                    return 2;
                }
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
                return 2;
            }

        }

        public String[] fileData()
        {

            ArrayList<String> data = new ArrayList<String>();
            try
            {
                URL url = new URL(SERVER_URL + "?"
                        + "User=" + username //username
                        + "&Pass=" + password //password
                        + "&Type=GET_FILE_LIST"); //sets request type
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("GET");

                InputStream inputStream = httpConn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream())); //READ HASHES IN THE SAME ORDER

                //READ HASHES LINE BY LINE
                String responseLine = reader.readLine();
                int i = 0;
                while (responseLine != null) //go through the file until end_of_file reached
                {
                    data.add(responseLine);//add current line
                    System.out.println(responseLine);
                    i++; //update iteration
                    responseLine = reader.readLine(); //next line please..
                }

                System.out.println(data);


                return data.toArray(new String[data.size()]); //give me the data table
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        public void getFile()
        {
            try
            {

                URL url = new URL(SERVER_URL);/* + "?"
                        + "User=" + username //username
                        + "&Pass=" + password //password
                        + "&Attributes1=" + a1 //FileIDs CSV
                        + "&Attributes2=" + a2 //filenames CSV
                        + "&Type=GET_HASHES"); //sets request type*/
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("POST");
                httpConn.setRequestProperty("User", username);
                httpConn.setRequestProperty("Pass", password);
                httpConn.setRequestProperty("Attributes1", fileID);
                httpConn.setRequestProperty("Attributes2", fileName);
                httpConn.setRequestProperty("Type", "GET_FILE");

                String remoteName = httpConn.getHeaderField("filename");
                String remoteFid = httpConn.getHeaderField("fid");
                byte[] buffer = new byte[4096];
                int bytesRead = -1;

                if (remoteName.endsWith(".html"))
                {
                    noteName = remoteName.substring(0, remoteName.length() - 5);
                }

                File file = new File(literaPath + "/" + noteName);
                if (!file.exists())
                {
                    file.mkdirs();
                    (new FileOutputStream(literaPath + "/" + noteName + "/id.lit")).write(remoteFid.getBytes());
                }

                FileOutputStream outputStream = new FileOutputStream(literaPath + "/" + noteName + "/" + fileName);
                InputStream inputStream = httpConn.getInputStream();

                int size = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1)
                {
                    outputStream.write(buffer, 0, bytesRead);
                    size += bytesRead;
                }

                outputStream.close();
                inputStream.close();

                System.out.println("File received. Size: " + size);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


    }

    class postThread implements Runnable
    {
        Thread t;
        String path;
        String fileID;
        String noteName;
        String hash;

        public void setPath(String p)
        {
            path = p;
        }

        public void setFileID(String fid)
        {
            fileID = fid;
            hash = generateHash(fid, (new File(path)).getName());
            noteName = noteName(fid);
        }

        public void run()
        {
            postFile();
        }

        public void start()
        {
            if (t == null)
            {
                t = new Thread(this, "post" + System.currentTimeMillis());
                t.start();
                System.out.println("File post started... : " + path);
            }
        }

        public void postFile()
        {
            try
            {
                File uploadFile = new File(path);

                URL url = new URL(SERVER_URL);

                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setUseCaches(false);
                httpConn.setDoOutput(true);
                httpConn.setRequestMethod("POST");

                //set user credential data in the http request for server side authorization
                httpConn.setRequestProperty("User", username);
                httpConn.setRequestProperty("Pass", password);
                //define file attributes...
                httpConn.setRequestProperty("fileName", uploadFile.getName());
                httpConn.setRequestProperty("fileID", fileID);
                httpConn.setRequestProperty("hash", hash);
                httpConn.setRequestProperty("Type", "POST_FILE");

                OutputStream outputStream = httpConn.getOutputStream();
                FileInputStream inputStream = new FileInputStream(uploadFile);

                byte[] buffer = new byte[4096];
                int bytesRead = -1;

                int size = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1)
                {
                    outputStream.write(buffer, 0, bytesRead);
                    size += bytesRead;
                }

                outputStream.close();
                inputStream.close();

                int serverStatus = httpConn.getResponseCode();
                if (serverStatus == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                    String response = reader.readLine();
                    System.out.print("Server's response: " + response);
                    System.out.println(" - File sent, size: " + size);
                }
                else if (serverStatus == 999)
                {
                    System.out.println(" LOGIN FAILED");
                }
                else
                {
                    System.out.print("Server error: " + serverStatus);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

}
