package softuni.exam.models.dtos.xmls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "offers")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferImportRootDto {
    @XmlElement(name = "offer")
    private List<OfferImportDto> offerImportDtos;

    public OfferImportRootDto() {
    }

    public List<OfferImportDto> getOfferImportDtos() {
        return offerImportDtos;
    }

    public void setOfferImportDtos(List<OfferImportDto> offerImportDtos) {
        this.offerImportDtos = offerImportDtos;
    }
}
