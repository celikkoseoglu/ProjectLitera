package litera.Data;
/*
*
* *
* @author Yigit Polat
*
*
*
 */


import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class ServerManager
{
    private String username;
    private String password;
    private String literaPath;
    private boolean isSignedIn;

    private ServliteraClient client;

    public ServerManager(String user, String pass)
    {
        username = user;
        password = pass;
        literaPath = LocalDataManager.getLocalNotesFilePath();
        client = new ServliteraClient(username, password, literaPath);
    }

    public int connect()
    {
        if (client.initializeConnection())
            if (isSignedIn = client.login())
                return 0;
            else
                return 1;
        else
            return 2;
    }

    public int setUser(String user, String pass)
    {
        username = user;
        password = pass;
        client = new ServliteraClient(username, password, literaPath);
        return connect();
    }

    public int createNewUser(String user, String pass, String email)
    {
        return client.createNewUser(user, pass, email);
    }


    // [0][i] -> fid
    // [1][i] -> fname
    public String[][] getDifferent()
    {
        String[][] toReturn;
        ArrayList<String> difFid = new ArrayList();
        ArrayList<String> difFname = new ArrayList();
        ArrayList<String> localData = new ArrayList();
        ArrayList<String> localHash = new ArrayList();
        String[][] servletFiles = client.filesAtServlitera(); //0:file id, 1:filename, 2:hash
        String[] fids = LocalDataManager.getFileIDs();


        for (int i = 0; i < fids.length; i++)
        {
            File[] files = (new File(literaPath + "/" + client.noteName(fids[i]))).listFiles();

            for (int j = 0; j < files.length; j++)
            {
                localData.add(fids[i] + files[j].getName());
                localHash.add(client.generateHash(fids[i], files[j].getName()));
            }
        }

        System.out.println("local data array: " + localData.size() + ":" + localHash.size());
        System.out.println("servlet data array: " + servletFiles[0].length);
        for (int i = 0; i < servletFiles[0].length; i++)
        {
            int loci = localData.indexOf(servletFiles[0][i] + servletFiles[1][i]);

            System.out.println("loci: " + loci);
            System.out.println("servlet fid: " + servletFiles[0][i]);
            System.out.println("servlet fname: " + servletFiles[1][i]);
            System.out.println("servlet:local hash: " + servletFiles[2][i] + ":" + (loci != -1 ? localHash.get(loci) : "empty"));

            if (loci != -1)
            {
                if (servletFiles[2][i].equals("OLD"))
                {
                    //
                    //
                    //
                    // option box show up...
                    //
                    //
                    int choice = JOptionPane.YES_NO_OPTION;
                    choice = JOptionPane.showConfirmDialog(null, "There's an old version of " + servletFiles[1] + " at the server.\n Do you want to replace it?", "Ding!", choice);
                    if (choice == JOptionPane.YES_OPTION)
                    {
                        difFid.add(servletFiles[0][i]);
                        difFname.add(servletFiles[1][i]);
                    }

                }
                else if (!servletFiles[2][i].equals(localHash.get(loci)))
                {
                    difFid.add(servletFiles[0][i]);
                    difFname.add(servletFiles[1][i]);
                }
            }
            else
            {
                difFid.add(servletFiles[0][i]);
                difFname.add(servletFiles[1][i]);
            }
        }


        System.out.println("fid list size: " + difFid.size());

        toReturn = new String[2][difFid.size()];
        toReturn[0] = difFid.toArray(new String[difFid.size()]);
        toReturn[1] = difFname.toArray(new String[difFname.size()]);

        return toReturn;
    }

    // [0][i] -> fid
    // [1][i] -> fname
    public String[][] getLocalDifferent()
    {
        String[][] toReturn;
        ArrayList<String> difFid = new ArrayList();
        ArrayList<String> difFname = new ArrayList();
        ArrayList<String> fnames = new ArrayList();
        ArrayList<String> fileIDs = new ArrayList();

        String[] hashesAtServlet;
        String[] fids = LocalDataManager.getFileIDs();


        for (int i = 0; i < fids.length; i++)
        {
            File[] files = (new File(literaPath + "/" + client.noteName(fids[i]))).listFiles();
            for (int j = 0; j < files.length; j++)
            {
                File file = files[j];
                fnames.add(file.getName());
                fileIDs.add(fids[i]);
            }
        }

        hashesAtServlet = client.getHashesFromServlitera(fileIDs.toArray(new String[fileIDs.size()]), fnames.toArray(new String[fileIDs.size()]));
        for (int i = 0; i < fnames.size(); i++)
        {
            if (!hashesAtServlet[i].equals(client.generateHash(fileIDs.get(i), fnames.get(i))))
            {
                difFid.add(fileIDs.get(i));
                difFname.add(fnames.get(i));
            }
        }


        System.out.println("different files' IDs: " + difFid);
        System.out.println("different fnames: " + difFname);

        toReturn = new String[2][difFid.size()];
        toReturn[0] = difFid.toArray(new String[difFid.size()]);
        toReturn[1] = difFname.toArray(new String[difFname.size()]);

        return toReturn;

    }

    public String[] getNoteList()
    {
        return LocalDataManager.getNoteNames(literaPath);
    }

    public String[] getIDList()
    {
        return LocalDataManager.getFileIDs();
    }

    public boolean uploadAll()
    {
        client.postNotes(getIDList());
        return true;
    }

    public boolean downloadAll()
    {
        client.getNotes(LocalDataManager.getFileIDs());
        return true;
    }

    public boolean downloadDifferent()
    {
        String[][] data = getDifferent();
        System.out.println("data array length: " + data[0].length);
        for (int i = 0; i < data[0].length; i++)
        {
            client.bringFile(data[0][i], data[1][i]);
        }
        return true;
    }

    public boolean uploadDifferent() //not working properly...
    {
        String[][] data = getLocalDifferent();
        System.out.println("data array length: " + data[0].length + ":" + data[1].length);
        for (int i = 0; i < data[0].length; i++)
        {
            client.sendFile(data[0][i], data[1][i]);
        }
        return true;
    }


}

