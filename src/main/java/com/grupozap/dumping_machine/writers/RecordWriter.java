package com.grupozap.dumping_machine.writers;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;

import java.io.IOException;

public class RecordWriter {
    private ParquetWriter writer;
    private final String path;
    private final String filename;

    public RecordWriter(Schema schema, String path, String filename, int blockSize, int pageSize) {
        this.path = path;
        this.filename = filename;

        try {
            this.writer = AvroParquetWriter.<GenericRecord>builder(new Path(path + filename))
                    .withSchema(schema)
                    .withCompressionCodec(CompressionCodecName.SNAPPY)
                    .withRowGroupSize(blockSize)
                    .withPageSize(pageSize)
                    .withDictionaryEncoding(true)
                    .withWriteMode(ParquetFileWriter.Mode.OVERWRITE)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(GenericRecord record) {
        try {
            this.writer.write(record);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return this.path;
    }

    public String getFilename() {
        return this.filename;
    }
}