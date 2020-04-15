import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nhom BTD on 14/04/2020
 */
public class Main {
    private static int readIntSpd(RandomAccessFile raf) throws IOException {
        int ch1 = raf.read();
        int ch2 = raf.read();
        int ch3 = raf.read();
        int ch4 = raf.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 0) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
//        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }
    private static int readShortSpd(RandomAccessFile raf) throws IOException {
        int ch1 = raf.read();
        int ch2 = raf.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return ((ch1 << 0) + (ch2 << 8));
    }
    public static void main(String[] args){
        try {
            if (args.length == 1) {
                System.out.println("Chua nhap ten file");
                return;
            }
            String file = args[0];
            int tongSoTu, viTriDanhSach, duLieuThua, doDaiHeader, chieuDaiTu, chieuDaiNghia;
            byte[] bs;
            String[] header;
            String tu = "",nghia = "";
            ThongTinTu ttTu;
            LangComparator langcomparator = new LangComparator();
            Converter converter= new Converter();

            RandomAccessFile rafInput = new RandomAccessFile(file + ".spd", "r");
            viTriDanhSach = readIntSpd(rafInput);
            tongSoTu = (int) (rafInput.length() - (long)viTriDanhSach) / 4;
            ThongTinTu[] danhSachTu = new ThongTinTu[tongSoTu];
            duLieuThua = readShortSpd(rafInput);
            doDaiHeader = readShortSpd(rafInput);
            System.out.println(doDaiHeader);
            bs = new byte[doDaiHeader];
            rafInput.read(bs);
            //header ma ngon ngu, sdk 4, Giong doc, Ten tu dien, tac gia, font chinh, kich thuoc chinh, font phu, kich thuoc phu
            header = (new String(bs, "UTF-8")).split("\u0000");

            RandomAccessFile rafOutputIndex = new RandomAccessFile(file + ".mspd_index", "rw");
            RandomAccessFile rafOutputData = new RandomAccessFile(file + ".mspd_data", "rw");

            rafOutputIndex.setLength(0L);
            rafOutputData.setLength(0L);
            rafOutputIndex.write("mSPD001".getBytes("UTF-8"));
            rafOutputData.write("mSPD001".getBytes("UTF-8"));
            rafOutputIndex.writeLong(0L);
            rafOutputIndex.writeLong(0L);

            for (int i = 0; i < tongSoTu; i++){
                chieuDaiTu = readShortSpd(rafInput);
                bs = new byte[chieuDaiTu];
                rafInput.read(bs, 0, chieuDaiTu);
                tu = new String(bs, 0, chieuDaiTu, "UTF-8");
                ttTu = new ThongTinTu();
                ttTu.tu = tu;
                ttTu.vitri = rafOutputIndex.getFilePointer();
                bs = tu.getBytes("UTF-8");
                rafOutputIndex.writeShort(bs.length);
                rafOutputIndex.write(bs);
                
                chieuDaiNghia = readIntSpd(rafInput);
                rafOutputIndex.writeLong(rafOutputData.getFilePointer());
                bs = new byte[chieuDaiNghia];
                rafInput.read(bs);
                nghia = converter.convert(new String(bs, 0, chieuDaiNghia, "UTF-8"));
                bs = nghia.getBytes("UTF-8");
                chieuDaiNghia = bs.length;
                rafOutputIndex.writeInt(chieuDaiNghia);
                rafOutputData.write(bs);
                
                danhSachTu[i] = ttTu;
            }
            rafInput.close();
            rafOutputData.close();
            rafOutputIndex.seek(7L);
            rafOutputIndex.writeLong(rafOutputIndex.length());
            rafOutputIndex.seek(rafOutputIndex.length());

            bs = header[4].getBytes("UTF-8");
            rafOutputIndex.writeInt(bs.length);
            rafOutputIndex.write(bs, 0, bs.length);

            Arrays.sort(danhSachTu, langcomparator);
            for(int i = 0; i < tongSoTu; ++i) {
                rafOutputIndex.writeLong(danhSachTu[i].vitri);
            }
            rafOutputIndex.close();
            System.out.println("Ket thuc");
        }
        catch (IOException ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, (String)null, ex);
        }
    }
}