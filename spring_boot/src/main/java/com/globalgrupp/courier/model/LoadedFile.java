package com.globalgrupp.courier.model;

import javax.persistence.*;

/**
 * Created by Lenovo on 27.01.2016.
 */
@Entity
@Table(name="files")
public class LoadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="files_id")
    private Long id;

    @Column(name="files_data")
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoadedFile() {
    }
}

