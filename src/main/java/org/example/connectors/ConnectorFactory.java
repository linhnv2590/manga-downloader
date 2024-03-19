package org.example.connectors;

import org.example.connectors.templates.*;
import org.example.utils.HttpUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConnectorFactory {
    private ConnectorFactory() {
    }

    public enum ConnectorType {
        BLOGTRUYEN(Collections.singletonList("https://blogtruyenmoi.com/")),
        CHAPVN(Collections.singletonList("https://chap.vn/")),
        KENHSINHVIEN(Collections.singletonList("https://kenhsinhvien.vn/")),
        NETTRUYEN(Collections.singletonList("https://nettruyenx.com/")),
        NHASACHMIENPHI(Collections.singletonList("https://nhasachmienphi.com/")),
        THIENHATRUYEN(Collections.singletonList("https://thienhatruyen.net/")),
        THEGIOIMANGA(Collections.singletonList("https://thegioimanga.vn/")),
        TRUYENQQ(Collections.singletonList("https://truyenqqvn.com/")),
        TRUYENQQVN(Collections.singletonList("https://truyenqqvn.vn/"));

        private final List<String> urls;

        ConnectorType(List<String> urls) {
            this.urls = urls;
        }

        public List<String> getUrls() {
            return urls;
        }
    }

    public static Connector getConnector(String url) {
        return Arrays.stream(ConnectorType.values())
                .filter(type -> HttpUtils.isSameConnectorDomain(url, type.getUrls()))
                .findFirst()
                .map(type -> {
                    switch (type) {
                        case BLOGTRUYEN:
                            return new BlogTruyenMoi(url);
                        case CHAPVN:
                            return new ChapVn(url);
                        case KENHSINHVIEN:
                            return new KenhSinhVien(url);
                        case NETTRUYEN:
                            return new NetTruyen(url);
                        case NHASACHMIENPHI:
                            return new NhaSachMienPhi(url);
                        case THIENHATRUYEN:
                            return new ThienHaTruyen(url);
                        case THEGIOIMANGA:
                            return new TheGioiManga(url);
                        case TRUYENQQ:
                            return new Truyenqq(url);
                        case TRUYENQQVN:
                            return new TruyenqqVn(url);
                        default:
                            return null;
                    }
                }).orElse(null);
    }
}
