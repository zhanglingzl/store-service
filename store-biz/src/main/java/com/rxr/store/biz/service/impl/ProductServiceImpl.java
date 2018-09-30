package com.rxr.store.biz.service.impl;

import com.google.common.collect.Lists;
import com.rxr.store.biz.repositories.AgencyRepository;
import com.rxr.store.biz.repositories.ProductQrCodeRepository;
import com.rxr.store.biz.repositories.ProductRepository;
import com.rxr.store.biz.service.ProductService;
import com.rxr.store.biz.util.KeyUtil;
import com.rxr.store.biz.util.QRCodeFactory;
import com.rxr.store.common.entity.Agency;
import com.rxr.store.common.entity.Product;
import com.rxr.store.common.entity.ProductQrCode;
import com.rxr.store.common.form.ProductForm;
import com.rxr.store.common.form.ProductQrCodeForm;
import com.rxr.store.common.util.DateHelper;
import com.rxr.store.common.util.FileHelper;
import com.rxr.store.common.util.StringHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author zero
 * @date Create in 2018-06-13 22:10
 */
@Service
public class ProductServiceImpl implements ProductService{
    @Value("store.wechatUrl")
    private static String wechatUrl;

    @Value("${store.imageTempPath}")
    private String imageTempPath;

    @Value("${store.imagePath}")
    private String imagePath;

    @Value("${store.imageUrl}")
    private String imageUrl;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private ProductQrCodeRepository qrCodeRepository;

    private static final String  UID_STATUS = "$$";

    @Override
    public List<Product> getProducts(ProductForm productForm) {
        List<Product> productList = repository.findAll((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if(StringUtils.isNotBlank(productForm.getProductName())){
                predicate.getExpressions().add(criteriaBuilder.like(root.get("productName"), "%" + productForm.getProductName() + "%"));
            }
            return predicate;
        });
        productList.forEach(product -> product.setImageUrl(imageUrl));
        return productList;
    }

    @Override
    public void saveOrUpdateProduct(Product product) {
        copyImage(product.getImages(), product.getProductNo());
        copyImage(product.getCover(), product.getProductNo());
        convertCoverAndImages(product);
        repository.save(product);
    }

    @Override
    public void createProductQrCode(ProductQrCodeForm qrCodeForm) {
        String number = qrCodeForm.getSerialNo();
        String url = "http://www.runxier.com/wechat/product/qrcode?serialNo=";
        String saveUrl = "/var/www/images/product/qrcode/"+ DateHelper.serialDateCode();
        List<ProductQrCode> qrCodes = new ArrayList<>();
        Optional<Agency> agency = this.agencyRepository.findById(1000L);
        Product product = this.repository.findByProductNo(qrCodeForm.getProductNo());
        int box = qrCodeForm.getProductBox();
        while(box > 0) {
            try {
                number = StringHelper.numberAddOne(number);
                ProductQrCode boxQrCode = new ProductQrCode();
                //boxQrCode.setAgency(agency.get());
                //boxQrCode.setProduct(product);
                // 箱
                boxQrCode.setType(2);
                boxQrCode.setSerialNo(qrCodeForm.getSerialNumber(number));
                boxQrCode.setQrContent(url+URLEncoder.encode(KeyUtil.encrypt(qrCodeForm.getSerialNumber(number)),"utf-8"));
                boxQrCode.setQrCodeUrl(saveUrl);
                qrCodes.add(boxQrCode);
                for(int i=0; i<6; i++) {
                    number = StringHelper.numberAddOne(number);
                    ProductQrCode caseQrCode = new ProductQrCode();
                    //caseQrCode.setAgency(agency.get());
                    //caseQrCode.setProduct(product);
                    //盒
                    caseQrCode.setType(1);
                    caseQrCode.setSerialNo(qrCodeForm.getSerialNumber(number));
                    caseQrCode.setQrContent(url+ URLEncoder.encode(KeyUtil.encrypt(
                            qrCodeForm.getSerialNumber(number)),"utf-8"));
                    caseQrCode.setQrCodeUrl(boxQrCode.getQrCodeUrl()+"/"+boxQrCode.getSerialNo());
                    caseQrCode.setParentSerialNo(caseQrCode.getSerialNo());
                    qrCodes.add(caseQrCode);
                    for(int j=0; j<50; j++ ) {
                        number = StringHelper.numberAddOne(number);
                        ProductQrCode oneQrCode = new ProductQrCode();
                        //oneQrCode.setAgency(agency.get());
                        //oneQrCode.setProduct(product);
                        //支
                        oneQrCode.setType(0);
                        oneQrCode.setSerialNo(qrCodeForm.getSerialNumber(number));
                        oneQrCode.setQrContent(url+URLEncoder.encode(KeyUtil.encrypt(
                                qrCodeForm.getSerialNumber(number)),"utf-8"));
                        oneQrCode.setQrCodeUrl(caseQrCode.getQrCodeUrl()+"/"+caseQrCode.getSerialNo());
                        oneQrCode.setParentSerialNo(oneQrCode.getSerialNo());
                        qrCodes.add(oneQrCode);
                    }
                }
                box--;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //qrCodeRepository.saveAll(qrCodes);
        QRCodeFactory factory = new QRCodeFactory();
        qrCodes.forEach(qrCode -> {
            try {
                factory.CreatQrImage(qrCode.getQrContent(),qrCode.getSerialNo(),"jpg",qrCode.getQrCodeUrl(),null);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

    @Override
    public Product findProductByProductNo(String productNo) {
        return this.repository.findByProductNo(productNo);
    }

    @Override
    public void deleteProductById(Long id) {
        this.repository.deleteById(id);
    }

    @Override
    public void saveProductImage(MultipartFile file, String uid) {
        try {
            FileHelper.writeFile(file.getInputStream(), imageTempPath + uid + Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().indexOf(".")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将封面及图片中的$$替换为空
     *  <pre>名称中包含$$为已经上传的文件</pre>
     * @param product 产品
     */
    private void convertCoverAndImages(Product product){
        product.setCover(product.getCover().replace(UID_STATUS, ""));
        product.setImages(product.getImages().replace(UID_STATUS, ""));
    }

    private void copyImage(String images, String productNo){
        Lists.newArrayList(images.split(",")).forEach(image -> {
            try {
                if(!image.contains(UID_STATUS)){
                    FileHelper.copyFile(imageTempPath + image, imagePath + productNo + File.separator + image);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
