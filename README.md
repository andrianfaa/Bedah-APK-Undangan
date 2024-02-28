# Bedah APK Undangan

Sebelum mulai bedahnya, disini saya mau menjelaskan bahwa niat saya membedah ini hanya untuk mendapatkan lokasi data korban dikirim ke mana dan oleh siapa di terimanya, jadi tidak ada yang aneh2 ya wkwkwk

## Step by step

Pertama, extract aplikasi (.apk) undangannya, setelah terekstract, kamu akan mendapatkan beberapa file berupa file .dex, .xml. dll. (bisa dilihat di folder `Undangan Pernikahan💌`)

setelah terekstract, pastikan ada file `classes3.dex`.

Kemudian buka link https://decompiler.com, upload file `classes3.dex` nya, dan tunggu hingga proses nya selesai, kamu bisa langsung liat isi filenya satu per-satu (atau download dulu juga gapapa, biar bisa liat langsung beberapa filenya). mungkin yang awam akan kebingungan dengan isi filenya, tapi kamu bisa filter dengan menggunakan filter bawaan browser kalian, tinggal tekan tombol `CTRL + F` dan ketik `https://`, biasanya akan langsung ketemu target nya

untuk contohnya, kalian bisa liat di folder `decompiled classes3.dex/sources/com/example/myapplicatior` yang ada di repository ini dan buka file `SendSMS.java` line ke-55

## Saran

Kalo kamu, teman kamu, atau keluarga kamu mendapatkan pesan berupa file .apk via whatsapp atau aplikasi lain dan ada tulisan **forward** (biasanya ada di whatsapp), langsung hapus saja, jangan coba-coba menginstallnya, kalau kamu tetap menginstallnya dan mungkin sudah membuka aplikasinya dan mengizinkan semua permissionnya, langsung matikan koneksi internet (data seluler, wifi) dan coba hapus aplikasi yang terinstall ini di pengaturan (**biasanya** aplikasinya tidak ada namanya).