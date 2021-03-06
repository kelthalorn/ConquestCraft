package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveFormatOld implements ISaveFormat
{
    private static final Logger logger = LogManager.getLogger();
    /** Reference to the File object representing the directory for the world saves */
    public final File savesDirectory;
    private static final String __OBFID = "CL_00000586";

    public SaveFormatOld(File p_i2147_1_)
    {
        if (!p_i2147_1_.exists())
        {
            p_i2147_1_.mkdirs();
        }

        this.savesDirectory = p_i2147_1_;
    }

    /**
     * Returns the name of the save format.
     */
    @SideOnly(Side.CLIENT)
    public String getName()
    {
        return "Old Format";
    }

    @SideOnly(Side.CLIENT)
    public List getSaveList() throws AnvilConverterException
    {
        ArrayList arraylist = Lists.newArrayList();

        for (int i = 0; i < 5; ++i)
        {
            String s = "World" + (i + 1);
            WorldInfo worldinfo = this.getWorldInfo(s);

            if (worldinfo != null)
            {
                arraylist.add(new SaveFormatComparator(s, "", worldinfo.getLastTimePlayed(), worldinfo.getSizeOnDisk(), worldinfo.getGameType(), false, worldinfo.isHardcoreModeEnabled(), worldinfo.areCommandsAllowed()));
            }
        }

        return arraylist;
    }

    public void flushCache() {}

    /**
     * Returns the world's WorldInfo object
     *  
     * @param saveName The name of the directory containing the world
     */
    public WorldInfo getWorldInfo(String saveName)
    {
        File file1 = new File(this.savesDirectory, saveName);

        if (!file1.exists())
        {
            return null;
        }
        else
        {
            File file2 = new File(file1, "level.dat");
            NBTTagCompound nbttagcompound;
            NBTTagCompound nbttagcompound1;

            if (file2.exists())
            {
                try
                {
                    nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
                    nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
                    return new WorldInfo(nbttagcompound1);
                }
                catch (Exception exception1)
                {
                    logger.error("Exception reading " + file2, exception1);
                }
            }

            file2 = new File(file1, "level.dat_old");

            if (file2.exists())
            {
                try
                {
                    nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
                    nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
                    return new WorldInfo(nbttagcompound1);
                }
                catch (Exception exception)
                {
                    logger.error("Exception reading " + file2, exception);
                }
            }

            return null;
        }
    }

    /**
     * Renames the world by storing the new name in level.dat. It does *not* rename the directory containing the world
     * data.
     *  
     * @param dirName The name of the directory containing the world.
     * @param newName The new name for the world.
     */
    @SideOnly(Side.CLIENT)
    public void renameWorld(String dirName, String newName)
    {
        File file1 = new File(this.savesDirectory, dirName);

        if (file1.exists())
        {
            File file2 = new File(file1, "level.dat");

            if (file2.exists())
            {
                try
                {
                    NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
                    NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
                    nbttagcompound1.setString("LevelName", newName);
                    CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file2));
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean func_154335_d(String p_154335_1_)
    {
        File file1 = new File(this.savesDirectory, p_154335_1_);

        if (file1.exists())
        {
            return false;
        }
        else
        {
            try
            {
                file1.mkdir();
                file1.delete();
                return true;
            }
            catch (Throwable throwable)
            {
                logger.warn("Couldn\'t make new level", throwable);
                return false;
            }
        }
    }

    /**
     * @args: Takes one argument - the name of the directory of the world to delete. @desc: Delete the world by deleting
     * the associated directory recursively.
     */
    public boolean deleteWorldDirectory(String p_75802_1_)
    {
        File file1 = new File(this.savesDirectory, p_75802_1_);

        if (!file1.exists())
        {
            return true;
        }
        else
        {
            logger.info("Deleting level " + p_75802_1_);

            for (int i = 1; i <= 5; ++i)
            {
                logger.info("Attempt " + i + "...");

                if (deleteFiles(file1.listFiles()))
                {
                    break;
                }

                logger.warn("Unsuccessful in deleting contents.");

                if (i < 5)
                {
                    try
                    {
                        Thread.sleep(500L);
                    }
                    catch (InterruptedException interruptedexception)
                    {
                        ;
                    }
                }
            }

            return file1.delete();
        }
    }

    /**
     * @args: Takes one argument - the list of files and directories to delete. @desc: Deletes the files and directory
     * listed in the list recursively.
     */
    protected static boolean deleteFiles(File[] p_75807_0_)
    {
        for (int i = 0; i < p_75807_0_.length; ++i)
        {
            File file1 = p_75807_0_[i];
            logger.debug("Deleting " + file1);

            if (file1.isDirectory() && !deleteFiles(file1.listFiles()))
            {
                logger.warn("Couldn\'t delete directory " + file1);
                return false;
            }

            if (!file1.delete())
            {
                logger.warn("Couldn\'t delete file " + file1);
                return false;
            }
        }

        return true;
    }

    /**
     * Returns back a loader for the specified save directory
     */
    public ISaveHandler getSaveLoader(String p_75804_1_, boolean p_75804_2_)
    {
        return new SaveHandler(this.savesDirectory, p_75804_1_, p_75804_2_);
    }

    @SideOnly(Side.CLIENT)
    public boolean func_154334_a(String saveName)
    {
        return false;
    }

    /**
     * gets if the map is old chunk saving (true) or McRegion (false)
     *  
     * @param saveName The name of the directory containing the world
     */
    public boolean isOldMapFormat(String saveName)
    {
        return false;
    }

    /**
     * converts the map to mcRegion
     *  
     * @param filename Filename for the level.dat_mcr backup
     */
    public boolean convertMapFormat(String filename, IProgressUpdate p_75805_2_)
    {
        return false;
    }

    /**
     * Return whether the given world can be loaded.
     */
    @SideOnly(Side.CLIENT)
    public boolean canLoadWorld(String p_90033_1_)
    {
        File file1 = new File(this.savesDirectory, p_90033_1_);
        return file1.isDirectory();
    }
}