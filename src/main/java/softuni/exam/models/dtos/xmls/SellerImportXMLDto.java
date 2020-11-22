package softuni.exam.models.dtos.xmls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "seller")
@XmlAccessorType(XmlAccessType.FIELD)
public class SellerImportXMLDto {
    @XmlElement
    private long id;

    public SellerImportXMLDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
