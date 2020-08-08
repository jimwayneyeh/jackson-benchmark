package tw.jimwayneyeh.benchmark.jacksonbestpractice.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class SimpleObject {
    private String s1;
    private String s2;
    private String s3;
    private String s4;
    private String s5;

    private Integer i1;
    private Integer i2;
    private Integer i3;
    private Integer i4;
    private Integer i5;

    private Long l1;
    private Long l2;
    private Long l3;
    private Long l4;
    private Long l5;

    private Float f1;
    private Float f2;
    private Float f3;
    private Float f4;
    private Float f5;

    private Double d1;
    private Double d2;
    private Double d3;
    private Double d4;
    private Double d5;
}
