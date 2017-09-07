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

    static getProduct(itemId:string) {
        for (let product of Product.getProducts()) {
            if (product.itemId == itemId) {
                return product;
            }
        }
    }

    static getProducts() {
        return [
            new Product("810198450", "Signature Design by Ashley Storey Living Room Sectional, 2-Piece Set", "Bring modern appeal with a comfortable design into your home with the Signature Design by Ashley® Storey sectional collection. Beautiful leather-like fabric in luscious brown brings a touch of sophistication to your living room's décor. And with its spacious design and generously sized ottoman, this set is great for entertaining your loved ones or frequent guests. Rich in relaxation and design, this set allows for everyone to have a cozy spot. Must purchase right arm facing sofa (SKU 810198450) and left arm facing chaise (SKU 810198581) to complete sectional set.",
                        "699.00", "799.99", "Save $100.99  (12%)", "http://images.biglots.com/Signature+Design+by+Ashley+Storey+Living+Room+Sectional%2C+2-Piece+Set?set=imageURL%5B%2Fimages%2Fproduct%2F148%2F810198450_810198581-2.jpg%5D,env%5Bprod%5D,nocache%5Btrue%5D,ver%5B1%5D,profile%5Bpdp_main_small%5D&call=url%5Bfile:biglots/product.chain%5D"),
            new Product("810324009", "Signature Design By Ashley Pindall Loveseat", "Sit back and relax in the most comfortable seat designed with love and contemporary style. The Pindall loveseat is made for easy, everyday living, upholstered in a sumptuous chenille fabric with a hint of texture. Divided back design puts pillow plushness and support right where it’s needed most. Specially constructed with overstuffed pillows, top armrests and toss pillows creating the ultimate seating experience. Other Pindall collection pieces sold separately.",
                        "310.00", "350.00", "Save $40.00  (11%)", "http://images.biglots.com/Pindall+Loveseat+Front+View+Silo+Image?set=imageURL%5B%2Fimages%2Fproduct%2F23%2F810324009.jpg%5D,env%5Bprod%5D,nocache%5Btrue%5D,ver%5B1%5D,profile%5Bpdp_main_large%5D&call=url%5Bfile:biglots/product.chain%5D"),                        
            new Product("810324010", "Signature Design By Ashley Pindall Accent Chair", "Relaxation is in order when the eye-catching Pindall chair is in the house. Crafted with a geometric print delivering modern attitude to all your home's furnishings. Constructed with sleek arms, comfortable box cushion and a slightly angled back lending support and flawless style. Other Pindall collection pieces sold separately.",
                        "299.99", "", "", "http://images.biglots.com/Pindall+Accent+Chair+Angled+View+Room+Setting+Lifestyle+Image?set=imageURL%5B%2Fimages%2Fproduct%2F25%2F810324010.jpg%5D,env%5Bprod%5D,nocache%5Btrue%5D,ver%5B1%5D,profile%5Bpdp_main_large%5D&call=url%5Bfile:biglots/product.chain%5D"),                                                
            new Product("810324802", "Simmons Morgan Antique Memory Foam Sofa", "Add a touch of sophisticated comfort to your living space with this gorgeous Morgan sofa by Simmons. The soft chenille fabric and nailhead trim bring out the beauty in any room and transform your space with an upscale feel to all your home's furnishings. Other Morgan collection pieces sold separately.",
                        "400.00", "450.00", "Save $50.00  (11%)", "http://images.biglots.com/Morgan+Antique+Memory+Foam+Sofa+Front+View+Silo+Image?set=imageURL%5B%2Fimages%2Fproduct%2F19%2F810324802.jpg%5D,env%5Bprod%5D,nocache%5Btrue%5D,ver%5B1%5D,profile%5Bpdp_main_large%5D&call=url%5Bfile:biglots/product.chain%5D"),                                                
        ];
    }
}



