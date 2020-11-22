package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.xmls.OfferImportDto;
import softuni.exam.models.dtos.xmls.OfferImportRootDto;
import softuni.exam.models.entities.Car;
import softuni.exam.models.entities.Offer;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

@Service
public class OfferServiceImpl implements OfferService {
    private final static String OFFER_XML_PATH = "src/main/resources/files/xml/offers.xml";
    private final OfferRepository offerRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final CarRepository carRepository;
    private final SellerRepository sellerRepository;

    public OfferServiceImpl(OfferRepository offerRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper, CarRepository carRepository, SellerRepository sellerRepository) {
        this.offerRepository = offerRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.carRepository = carRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(OFFER_XML_PATH)));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        OfferImportRootDto offerImportRootDto = this.xmlParser.parseXml(OfferImportRootDto.class, OFFER_XML_PATH);
        for (OfferImportDto offerImportDto : offerImportRootDto.getOfferImportDtos()) {
            if (validationUtil.isValid(offerImportDto)) {
                Offer offer = this.modelMapper.map(offerImportDto, Offer.class);
                Car car = this.carRepository.findById(offerImportDto.getCar().getId()).get();
                Seller seller = this.sellerRepository.findById(offerImportDto.getSeller().getId()).get();

                offer.setCar(car);
                offer.setSeller(seller);
                offer.setPictures(new HashSet<>(car.getPictures()));

                this.offerRepository.saveAndFlush(offer);

                sb.append(String.format("Successfully imported offer %s - %s",
                        offer.getAddedOn(), offer.isHasGoldStatus()))
                        .append(System.lineSeparator());
            } else {
                sb.append("Invalid offer")
                        .append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
