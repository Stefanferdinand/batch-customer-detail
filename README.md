# Spring Batch - Customer Details and Transactions Data Synchronization

Sebuah perusahaan e-commerce memiliki banyak transaksi data setiap harinya. Perusahaan e-commerce ini memiliki tujuan untuk membuat laporan yang tersinkronisasi dengan database per hari. Laporan-laporan yang dibutuhkan adalah transaksi harian dan detil informasi pelanggan yang melakukan transaksi pada hari itu. Adapun laporan detil informasi semua pelanggan beserta total pengeluarannya dari awal transaksi; yang kemungkinan dibutuhkan bila diminta. Semua laporan-laporan ini dibentuk dalam format CSV.

## Usage

![image](https://github.com/Stefanferdinand/batch-customer-detail/assets/40900984/24a019b0-8829-43ea-a957-e634ab0126c5)

### Endpoint untuk Job:
-  `/triggerTransactionJob`: Untuk menjalankan job (Nama Job: `TransactionJob`) yang membuat laporan transaksi harian
-  `/triggerCustomerDailyJob`: Untuk menjalankan job (Nama Job: `CustDailyJob`) yang membuat laporan detil informasi pelanggan yang melakukan transaksi per hari job dijalankan
-  `/triggerCustomerJob`: Untuk menjalankan job (Nama Job: `CustomerJob`) yang membuat laporan detil informasi semua pelanggan beserta total pengeluarannya dari awal transaksi

### Endpoint untuk Troubleshooting:
-  `/retryAllFailedJob`: Untuk menjalankan semua job yang gagal berjalan
-  `/retryFailedJob/{instanceId}`: Untuk menjalankan job sesuai ID-nya (Untuk sekarang hanya bisa dijalankan oleh developer)

## How it works

Flowchart ini berlaku pada job-job yang tertera pada [yang tertera di atas](#endpoint-untuk-job). Masing-masing job memiliki schedulernya sendiri. Ketika ada permintaan dari API atau scheduler, job akan mulai dijalankan untuk menarik data sesuai kebutuhan. Bila ada error, maka ditunjukkan show runtime exception. Bila tidak, maka data diturunkan bila tidak kosong.
![image](https://github.com/Stefanferdinand/batch-customer-detail/assets/40900984/e0320932-b431-4303-ac95-8628736433f6)

## Teams
- I Made Ari Widiarsana:
  Contribution:
  - Membuat job `TransactionJob`
  - Membuat Unit Testing `TransactionJob`
  - Quality Assurance kode
  - Migrasi data-data yang dibutuhkan ke database
  - Bertanggung jawab dalam membuat cron job

- Michael Chuang:
  Contribution:
  - Membuat job `CustomerJob` dan `CustDailyJob`
  - Membuat Unit Testing `CustomerJob` dan `CustDailyJob`
  - Memfasilitasi database
  - Bertanggung jawab dalam melakukan merge pada setiap branch

- Stefan Ferdinand
  Contribution:
  - Membuat REST API untuk menjalankan job
  - Melakukan error handling
  - Memfasilitasi Github
  - Quality Assurance kode
