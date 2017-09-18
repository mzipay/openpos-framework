export class ProductCategory {
    constructor(description:string, categoryId:string, imageUrl:string, headerUrl:string) {
        this.categoryId = categoryId;
        this.description = description;
        this.imageUrl = imageUrl;
        this.headerUrl = headerUrl;
    }

    categoryId:string;
    description:string;
    imageUrl:string;
    headerUrl:string;

    static getCategories() {
        return [
            new ProductCategory("Furniture Collections", "furniture-collections", "http://1.shard.www.biglots.com.edgesuite.net/i/w/v_oENGZBY.jpg", "http://images.biglots.com/073017-headerpagetitle-furniture-furniturecollections-bedroom?set=imageURL%5B%2Fimages%2Fmarketing%2F2017%2F073017-headerpagetitle-furniture-furniturecollections-bedroom.jpg%5D,env%5Bprod%5D,width%5B940%5D&call=url%5Bfile:biglots/marketing.chain%5D"),
            new ProductCategory("Living Room", "living-room", "http://1.shard.www.biglots.com.edgesuite.net/a/p/c6cVF8fso.jpg", "http://images.biglots.com/073017-headerpagetitle-furniture-furniturecollections-bedroom?set=imageURL%5B%2Fimages%2Fmarketing%2F2017%2F073017-headerpagetitle-furniture-furniturecollections-bedroom.jpg%5D,env%5Bprod%5D,width%5B940%5D&call=url%5Bfile:biglots/marketing.chain%5D"),
            new ProductCategory("Chairs & Recliners", "chairs-recliners", "http://1.shard.www.biglots.com.edgesuite.net/4/K/e9sB9zAVY.jpg", "http://images.biglots.com/073017-headerpagetitle-furniture-furniturecollections-bedroom?set=imageURL%5B%2Fimages%2Fmarketing%2F2017%2F073017-headerpagetitle-furniture-furniturecollections-bedroom.jpg%5D,env%5Bprod%5D,width%5B940%5D&call=url%5Bfile:biglots/marketing.chain%5D"),
            new ProductCategory("Bedroom Furniture", "bedroom-furniture", "http://1.shard.www.biglots.com.edgesuite.net/D/i/KQC_7X1Es.jpg", "http://1.shard.www.biglots.com.edgesuite.net/D/i/KQC_7X1Es.jpg"),
            new ProductCategory("Ottomans & Poufs", "bedroom-furniture", "http://2.shard.www.biglots.com.edgesuite.net/G/e/WfO5Xtw5w.jpg", "http://1.shard.www.biglots.com.edgesuite.net/D/i/KQC_7X1Es.jpg"),
            new ProductCategory("Kitchen and Dining", "bedroom-furniture", "http://2.shard.www.biglots.com.edgesuite.net/9/d/s6UxLSv7Q.jpg", "http://1.shard.www.biglots.com.edgesuite.net/D/i/KQC_7X1Es.jpg"),
            
        ];
    }
}



