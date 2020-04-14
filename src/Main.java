import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

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
//    public static final int byteArrayToInt(byte[] b) {
//        return (b[0] << 24) + ((b[1] & 255) << 16) + ((b[2] & 255) << 8) + (b[3] & 255);
//    }
    public static final int byteArrayToInt(byte[] b, int start) {
        return (b[0 + start] << 24) + ((b[1 + start] & 255) << 16) + ((b[2 + start] & 255) << 8) + (b[3 + start] & 255);
//        return (b[3 + start] << 24) + ((b[2 + start] & 255) << 16) + ((b[1 + start] & 255) << 8) + (b[0 + start] & 255);
    }
    public static void main(String[] args){
        try {
            String file = "Thuatngunganhdien";
            int tongSoTu, viTriDanhSach, duLieuThua, doDaiString, chieuDaiTu, chieuDaiNghia;
            String sold = "", dau = "";
            byte[] bs;
            String[] dln;
            boolean vn;
            long seek;
            String tu = "",nghia = "";
            ThongTinTu ttTu;
            LangComparator langcomparator = new LangComparator("en");;

            RandomAccessFile rafInput = new RandomAccessFile(file + ".spd", "r");
            viTriDanhSach = readIntSpd(rafInput);
            tongSoTu = (int) (rafInput.length() - (long)viTriDanhSach) / 4;
            ThongTinTu[] danhSachTu = new ThongTinTu[tongSoTu];
            duLieuThua = readShortSpd(rafInput);
            doDaiString = readShortSpd(rafInput);
            System.out.println(doDaiString);
            bs = new byte[doDaiString];
            rafInput.read(bs);
            //dln ma ngon ngu, sdk 4, Giong doc, Ten tu dien, tac gia, font chinh, kich thuoc chinh, font phu, kich thuoc phu
            dln = (new String(bs, "UTF-8")).split("\u0000");

            RandomAccessFile var10 = new RandomAccessFile(file + ".mspd_index", "rw");
            RandomAccessFile var11 = new RandomAccessFile(file + ".mspd_data", "rw");

            var10.setLength(0L);
            var11.setLength(0L);
            var10.write("mSPD001".getBytes("UTF-8"));
            var11.write("mSPD001".getBytes("UTF-8"));
            var10.writeLong(0L);
            var10.writeLong(0L);

            for (int i = 0; i < tongSoTu; i++){
                chieuDaiTu = readShortSpd(rafInput);
                bs = new byte[chieuDaiTu];
                //doc tu tu spd
                rafInput.read(bs);
                tu = new String(bs, 0, chieuDaiTu, "UTF-8");
                ttTu = new ThongTinTu();
                ttTu.tu = tu;
                ttTu.vitri = var10.getFilePointer();
                //ghi tu vao mspd
                bs = tu.getBytes("UTF-8");
                var10.writeShort((short)chieuDaiTu);
                var10.write(bs);
                
                chieuDaiNghia = readIntSpd(rafInput);
                var10.writeLong(var11.getFilePointer());
                bs = new byte[chieuDaiNghia];
                rafInput.read(bs);
                nghia = new String(bs, 0, chieuDaiNghia, "UTF-8");
                bs = nghia.getBytes("UTF-8");
                var10.writeInt(chieuDaiNghia);
                var11.write(bs, 0, chieuDaiNghia);
                
                danhSachTu[i] = ttTu;
            }
            rafInput.close();
            var11.close();
            var10.seek(7L);
            var10.writeLong(var10.length());
            var10.seek(var10.length());

            bs = "Tung Lam test".getBytes("UTF-8");
            var10.writeInt(bs.length);
            var10.write(bs);

            Arrays.sort(danhSachTu, langcomparator);
            for(int i = 0; i < tongSoTu; ++i) {
                var10.writeLong(danhSachTu[i].vitri);
            }

//            System.out.println(tu + " " + nghia);
            System.out.println("Ket thuc");


        }
        catch (IOException ex){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, (String)null, ex);
        }
    }
}