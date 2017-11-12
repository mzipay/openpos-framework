export class Product {
    constructor(itemId:string, shortDescription:string, longDescription:string, price:string, regularPrice:string, savings:string, imageUrl:string) {
        this.itemId = itemId;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.price = price;
        this.regularPrice = regularPrice;
        this.savings = savings;
        this.imageUrl = imageUrl;
    }

    itemId:string;
    shortDescription:string;
    longDescription:string;
    price:string;
    regularPrice:string;
    savings:string;
    imageUrl:string;

    getAbreviatedLongDescription() {
        return this.longDescription.substring(0,this.longDescription.length*0.20) + "...";
    }

    static getProduct(itemId:string) {
        for (let product of Product.getProducts()) {
            if (product.itemId == itemId) {
                return product;
            }
        }
    }

    static getProducts() {
        return [
            new Product("810198450", "Ashley Storey Living Room Sectional, 2-Piece Set", "Bring modern appeal with a comfortable design into your home with the Signature Design by Ashley® Storey sectional collection.",
                        "400.00", "500.00", "Save $100.00  (20%)", "http://images.biglots.com/Signature+Design+by+Ashley+Storey+Living+Room+Sectional%2C+2-Piece+Set?set=imageURL%5B%2Fimages%2Fproduct%2F148%2F810198450_810198581-2.jpg%5D,env%5Bprod%5D,nocache%5Btrue%5D,ver%5B1%5D,profile%5Bpdp_main_small%5D&call=url%5Bfile:biglots/product.chain%5D"),
            new Product("810324010", "Signature Design By Ashley Pindall Accent Chair", "Relaxation is in order when the eye-catching Pindall chair is in the house. Crafted with a geometric print delivering modern attitude to all your home's furnishings.",
                        "299.99", "", "", "http://images.biglots.com/Pindall+Accent+Chair+Angled+View+Room+Setting+Lifestyle+Image?set=imageURL%5B%2Fimages%2Fproduct%2F25%2F810324010.jpg%5D,env%5Bprod%5D,nocache%5Btrue%5D,ver%5B1%5D,profile%5Bpdp_main_large%5D&call=url%5Bfile:biglots/product.chain%5D"),                                                
            new Product("810324802", "Simmons Morgan Antique Memory Foam Sofa", "Add a touch of sophisticated comfort to your living space with this gorgeous Morgan sofa by Simmons. The soft chenille fabric and nailhead trim bring out the beauty in any room and transform your space with an upscale feel to all your home's furnishings.",
                        "450.00", "500.00", "Save $50.00  (10%)", "http://images.biglots.com/Morgan+Antique+Memory+Foam+Sofa+Front+View+Silo+Image?set=imageURL%5B%2Fimages%2Fproduct%2F19%2F810324802.jpg%5D,env%5Bprod%5D,nocache%5Btrue%5D,ver%5B1%5D,profile%5Bpdp_main_large%5D&call=url%5Bfile:biglots/product.chain%5D"),                               
            new Product("810324009", "Signature Design By Ashley Pindall Loveseat", "Sit back and relax in the most comfortable seat designed with love and contemporary style. The Pindall loveseat is made for easy, everyday living, upholstered in a sumptuous chenille fabric with a hint of texture. ",
                        "350.00", "390.00", "Save $40.00  (10%)", "http://images.biglots.com/Pindall+Loveseat+Front+View+Silo+Image?set=imageURL%5B%2Fimages%2Fproduct%2F23%2F810324009.jpg%5D,env%5Bprod%5D,nocache%5Btrue%5D,ver%5B1%5D,profile%5Bpdp_main_large%5D&call=url%5Bfile:biglots/product.chain%5D"),                                         

        ];
    }


    static getProductFeatures(itemId:string) {
        switch (itemId) {
            case "810198450":
                return [
                    "Two-tone faux leather",
                    "Attached back cushions",
                    "Padded arms for more comfort",
                    "Stylish exposed wedge feet",
                    "Cushion cores are constructed of low melt fiber wrapped over high quality foam",
                    "Pillows not included",
                    "Fabric: 95% Polyester, 5% Polyurethane; Faux Leather: 100% Polyurethane",
                    "True color: Brown",
                    "Proudly made in the U.S.A. with domestic and imported material"
                ];
            case "810324009":
                return [
                    "Corner-blocked frame",
                    "Attached back and loose seat cushions",
                    "High-resilency foam cushions",
                    "2 - Decorative pillows included",
                    "Proudly made in the U.S.A. with domestic and imported material",
                    "Material: 100% Polyester",
                    "66\"W x 38\"D x 38\"H",
                ];
                case "810324010":
                return [
                    "Tight back and loose seat cushions",
                    "Corner-blocked frame",
                    "High-resilency foam cushions",
                    "Geometric woven fabric",
                    "Proudly made in the U.S.A. with domestic and imported material",
                    "Material: 100% Polyester",
                    "42\"W x 39\"D x 40\"H",
                ];      
                default:
                return [
                    "Features \"Comfort Plus\" memory foam",
                    "4 - Decorative pillows included",
                    "Soft, chenille, easy care cover with nailhead trim detail",
                    "Hardwoods used for a solid frame construction",
                    "Simmons Upholstery manufactured under license by United Furniture Industries",
                    "Proudly made in the U.S.A. with domestic and imported material",
                    "93\"L x 39\"D x 40\"H",
                ];                                
        }
    }
    
    
    static getProductSpecifications(itemId:string) {
        
        switch (itemId) {
            case "810198450":
                return [
                    "Frames: Limited Lifetime Warranty against manufacturer's defects on all wood frames",
                    "Cushioning: Limited Lifetime Warranty against manufacturer's defects",
                    "Springs: 1 year on breakage caused by metal fatigue",
                    "Fabrics: 1 year on fraying or seam slippage",
                    "The warranty does not cover normal wear and tear, or any physical damage or improper use",
                    "The warranty does not cover any condition resulting from incorrect or inadequate maintenance, cleaning or care",
                    "Warranty claims must be handled by the original retail dealer",
                    "Must provide proof of purchase and the serial number from the tags attached to the item",
                    "Some parts are available and can be ordered from the manufacturer by the original dealer",
                    "Product dimensions: 38”L x 84”W x 38”H",
                ];
            case "810324009":
                return [
                    "Frames: Limited Lifetime Warranty against manufacturer's defects on all wood frames",
                    "Cushioning: 1 year against manufacturer's defects",
                    "Springs: 1 year on breakage caused by metal fatigue",
                    "Fabrics: 1 year on fraying or seam slippage",
                    "The warranty does not cover normal wear and tear, or any physical damage or improper use",
                    "The warranty does not cover any condition resulting from incorrect or inadequate maintenance, cleaning or care",
                    "Warranty claims must be handled by the original retail dealer",
                    "Must provide proof of purchase and the serial number from the tags attached to the item",
                    "Some parts are available and can be ordered from the manufacturer by the original dealer",
                    "Model number: 7300435",
                ];
                case "810324010":
                return [
                    "Frames: Limited Lifetime Warranty against manufacturer's defects on all wood frames",
                    "Cushioning: 1 year against manufacturer's defects",
                    "Springs: 1 year on breakage caused by metal fatigue",
                    "Fabrics: 1 year on fraying or seam slippage",
                    "The warranty does not cover normal wear and tear, or any physical damage or improper use",
                    "The warranty does not cover any condition resulting from incorrect or inadequate maintenance, cleaning or care",
                    "Warranty claims must be handled by the original retail dealer",
                    "Must provide proof of purchase and the serial number from the tags attached to the item",
                    "Some parts are available and can be ordered from the manufacturer by the original dealer",
                    "Model number: 7300421",
                ];      
                default:
                return [
                    "Frames: Limited Lifetime Warranty against manufacturer's defects on all wood frames",
                    "Cushioning: Limited Lifetime Warranty against manufacturer's defects",
                    "Springs: 1 year on breakage caused by metal fatigue",
                    "Fabrics: 1 year on fraying or seam slippage",
                    "The warranty does not cover normal wear and tear, or any physical damage or improper use",
                    "The warranty does not cover any condition resulting from incorrect or inadequate maintenance, cleaning or care",
                    "Warranty claims must be handled by the original retail dealer",
                    "Must provide proof of purchase and the serial number from the tags attached to the item",
                    "Material: 100% Polyester",
                    "Some parts are available and can be ordered from the manufacturer by the original dealer",
                ];                                
        }
    }
}



