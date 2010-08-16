object PageContainer2: TPageContainer2
  Left = -4
  Top = -23
  Caption = 'site.dfm'
  ClientHeight = 526
  ClientWidth = 1280
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clBlack
  Font.Height = -16
  Font.Name = 'Arial'
  Font.Style = []
  KeyPreview = True
  OldCreateOrder = False
  Position = poDesigned
  Scaled = False
  Visible = True
  WindowState = wsMaximized
  PixelsPerInch = 96
  TextHeight = 18
  object index: TdhPage
    Left = 0
    Top = 0
    ImageType = bitTile
    Style.BackgroundImage.Data = {
      0954506E67496D61676589504E470D0A1A0A0000000D49484452000004F00000
      00010802000000DF20C7FB000000BF4944415478DAE596410E84300C03E3AFF2
      B27DD9BE650F70A034A10EB891AAE500951532958549B17DBE76BAD03C3AFDAA
      C2D7FB7ADCF64970B1DF0AB9CDB284EBA81417814E7111558FD108F4BEDEDB2E
      FC521D3778A18A8BB0CF2C2E067DF4DC4C2A5F7DA5FEB28A7B3542C9F56DE555
      1517617D9E6BEBA692FC8127B85C2A955C58E3B3824B0EE83CD79EA4C337E28F
      524946E321F7F5804E7345A9248FAFF25452DC9552B9CAB1D987CC4DC7214F1F
      58A64E659A5B3BA0EDC6D431F707218902FF9426073F0000000049454E44AE42
      6082}
    Style.BackgroundRepeat = cbrRepeatY
    Style.BackgroundColor = 14590050
    Style.FontFamily = 'Arial'
    AutoSizeXY = asNone
    Right = 0
    Bottom = 0
    UseIFrame = False
    Title = 'PingPong - manage null pointers - control NPEs'
    FTPURL = 'ftp://p8288468:5yMTPDtZ@www.kiegeland.com/pingpong/'
    HTTPURL = 'http://pingpong.kiegeland.com'
    GeneratedCSSFile = 'dfm2html.css'
    object Panel1: TdhPanel
      Left = 0
      Top = 0
      Width = 1232
      Height = 676
      Style.Margin = '16'
      Style.BackgroundColor = 33554431
      AutoSizeXY = asNone
      object Label2: TdhLabel
        Left = 32
        Top = 32
        Width = 769
        Height = 41
        Text = 
          'PingPong <small>manage null pointers, control NPEs <i><i><Label1' +
          '1><sup> for Java/Eclipse programmers</sup></Label11></i></i></sm' +
          'all>'
        Style.FontSize = '36'
        Style.FontFamily = 'arial'
        Style.Effects.AntiAliasing = True
        AutoSizeXY = asXY
      end
      object Link1: TdhLink
        Left = 32
        Top = 240
        Width = 33
        Height = 18
        Text = 'FAQ'
        Use = Link4
        AutoSizeXY = asXY
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
        LinkPage = faq
      end
      object Link2: TdhLink
        Left = 32
        Top = 200
        Width = 72
        Height = 18
        Text = 'Installation'
        Use = Link4
        AutoSizeXY = asXY
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
        LinkPage = install
      end
      object Link3: TdhLink
        Left = 32
        Top = 120
        Width = 79
        Height = 18
        Text = 'Introduction'
        Use = Link4
        AutoSizeXY = asXY
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
        LinkPage = usage
      end
      object Link6: TdhLink
        Left = 32
        Top = 320
        Width = 40
        Height = 18
        Text = 'Order'
        Style.Visibility = cviHidden
        AutoSizeXY = asXY
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
      end
      object Panel2: TdhPanel
        Left = 160
        Top = 104
        Height = 540
        Style.Border.Width = 1
        Style.Border.Color = 14590050
        Style.Border.Style = cbsSolid
        Style.BackgroundColor = 33554431
        AutoSizeXY = asNone
        Anchors = [akLeft, akTop, akRight]
        Right = 32
        object PageControl1: TdhPageControl
          Left = 440
          Top = 8
          Width = 24
          Height = 24
          ActivePage = install
          FixedHeight = False
          object usage: TdhPage
            Left = 8
            Top = 16
            Height = 2200
            AutoSizeXY = asNone
            Anchors = [akLeft, akTop, akRight]
            Right = 8
            UseIFrame = False
            object Label10: TdhLabel
              Left = 8
              Top = 16
              Width = 920
              Height = 108
              Text = 
                'If an object returned by a method call is not checked for null, ' +
                'accessing this object can raise a NullPointerException (NPE), wh' +
                'ich often interrupts the application in a way that some function' +
                'ality cannot be used anymore. This is a well known problem for c' +
                'omputer programs. PingPong tries to offer a solution for program' +
                's which are written in the Java programming language and which a' +
                're developed using Eclipse. '#10'<br/><br/>'#10'The following example pr' +
                'oduces a NPE in line 24, if methodA returns null, which is decid' +
                'ed on base of a random boolean value:'
              AutoSizeXY = asY
            end
            object Image1: TdhLink
              Left = 16
              Top = 136
              Width = 484
              Height = 190
              ImageType = bitImage
              Style.BackgroundImage.Path = 'images\pic1.GIF'
              Style.BackgroundImage.State = isAnalyzed
              Style.BackgroundImage.Width = 484
              Style.BackgroundImage.Height = 190
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
            end
            object Label12: TdhLabel
              Left = 8
              Top = 360
              Width = 952
              Height = 18
              Text = 
                'To manage such situations, methodA now declares that it can retu' +
                'rn null:'
              AutoSizeXY = asY
            end
            object Link8: TdhLink
              Left = 16
              Top = 392
              Width = 487
              Height = 188
              ImageType = bitImage
              Style.BackgroundImage.Path = 'images\pic2.GIF'
              Style.BackgroundImage.State = isAnalyzed
              Style.BackgroundImage.Width = 487
              Style.BackgroundImage.Height = 188
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
            end
            object Label13: TdhLabel
              Left = 8
              Top = 600
              Width = 952
              Height = 36
              Text = 
                'As doing so, the PingPong compiler now displays an error in line' +
                ' 26, since s is accessed but s can be null as storing the result' +
                ' of methodA. This can be solved by checking variable s on null:'
              AutoSizeXY = asY
            end
            object Link9: TdhLink
              Left = 16
              Top = 648
              Width = 505
              Height = 205
              ImageType = bitImage
              Style.BackgroundImage.Path = 'images\pic3.GIF'
              Style.BackgroundImage.State = isAnalyzed
              Style.BackgroundImage.Width = 505
              Style.BackgroundImage.Height = 205
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
            end
            object Label14: TdhLabel
              Left = 32
              Top = 888
              Width = 536
              Height = 100
              Text = 
                'Imagine methodA did a "Ping" by saying that its returned value m' +
                'ust be checked on null. Now, code not checking the returned valu' +
                'e on null but using it must do a "Pong" since they have a compil' +
                'ation error. So this code has to do a null check as in the prece' +
                'ding example or it can trigger another "Ping", which continues t' +
                'he PingPong game.'
              Style.Border.Color = 14590050
              Style.Border.Style = cbsSolid
              Style.Padding = 9
              Style.FontSize = '14'
              Style.Margin = '0'
              Style.TextAlign = ctaJustify
              AutoSizeXY = asY
            end
            object Label15: TdhLabel
              Left = 8
              Top = 1040
              Width = 952
              Height = 36
              Text = 
                'Now suppose the programmer wants to annotate for each method of ' +
                'a Java class whether it can return null or not,'#10'which can be don' +
                'e with annotations CanBeNull or NonNull. Suppose that he declare' +
                'd methodA as never returning null:'
              AutoSizeXY = asY
            end
            object Link10: TdhLink
              Left = 16
              Top = 1088
              Width = 489
              Height = 188
              ImageType = bitImage
              Style.BackgroundImage.Path = 'images\pic4.GIF'
              Style.BackgroundImage.State = isAnalyzed
              Style.BackgroundImage.Width = 489
              Style.BackgroundImage.Height = 188
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
            end
            object Label16: TdhLabel
              Left = 8
              Top = 1288
              Width = 952
              Height = 108
              Text = 
                'So actually the compiler error is no longer on line 26, at the c' +
                'aller method, but on line 20, at the called method. The error is' +
                ' that it declares to return only non-null values, but actually c' +
                'an return null in line 20. So this was obviously not the right a' +
                'nnotation to choose!'#10'<br/><br/>'#10'To fasten the process of definin' +
                'g the correct annotation for each method returning a value, '#10'it ' +
                'is recommended to define the @NonNullByDefault on the Java class' +
                ', meaning that every method'#10'having not defined an annotation exp' +
                'licitely is treated as having defined the @NonNull annotation:'
              AutoSizeXY = asY
            end
            object Link11: TdhLink
              Left = 16
              Top = 1408
              Width = 487
              Height = 239
              ImageType = bitImage
              Style.BackgroundImage.Path = 'images\pic5.GIF'
              Style.BackgroundImage.State = isAnalyzed
              Style.BackgroundImage.Width = 487
              Style.BackgroundImage.Height = 239
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
            end
            object Label17: TdhLabel
              Left = 8
              Top = 1664
              Width = 952
              Height = 36
              Text = 
                'The steps from above would typically follow. However to add the ' +
                '@CanBeNull annotation, one can apply the first proposed fix to t' +
                'he compilation error. So making your code null pointer aware can' +
                ' be done in the programming-by-fixing manner.'
              AutoSizeXY = asY
            end
            object Label18: TdhLabel
              Left = 8
              Top = 2024
              Width = 952
              Height = 36
              Text = 
                'Variables, method parameters and field members can also be assig' +
                'ned the CanBeNull and NonNull annotations. The NonNullAsDefault ' +
                'annotation applies also to method parameters and class fields, b' +
                'ut not to local variables.'
              AutoSizeXY = asY
            end
            object Image2: TdhLink
              Left = 16
              Top = 1712
              Width = 734
              Height = 299
              ImageType = bitImage
              Style.BackgroundImage.Path = 'images\pic6.GIF'
              Style.BackgroundImage.State = isAnalyzed
              Style.BackgroundImage.Width = 734
              Style.BackgroundImage.Height = 299
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
            end
          end
          object install: TdhPage
            Left = 8
            Top = 16
            Height = 512
            AutoSizeXY = asNone
            Anchors = [akLeft, akTop, akRight]
            Right = 8
            UseIFrame = False
            object Label6: TdhLabel
              Left = 16
              Top = 16
              Width = 191
              Height = 18
              Text = 'Update Site for Eclipse 3.5:'
              AutoSizeXY = asXY
            end
            object Link5: TdhLink
              Left = 216
              Top = 16
              Width = 286
              Height = 18
              Text = 'http://pingpong.kiegeland.com/3.5/update'
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
              Link = 'http://TODO.de/updatesite'
            end
            object Label9: TdhLabel
              Left = 16
              Top = 48
              Width = 392
              Height = 90
              Text = 
                'After installation, you should restart Eclipse and make sure, th' +
                'at Preferences -> JDT Weaving is ENABLED.'#10'<br/><br/>'#10'The PingPon' +
                'g annotations must be downloaded separately, see <Link15>Q2</Lin' +
                'k15>.'
              AutoSizeXY = asY
            end
            object StyleSheet4: TdhStyleSheet
              Left = 632
              Top = 112
              Width = 28
              Height = 28
              Expanded = False
              ExpandedWidth = 100
              ExpandedHeight = 100
              Expanded = False
              object Link15: TdhLink
                Left = 0
                Top = 0
                Height = 18
                Text = 'Q2'
                AutoSizeXY = asXY
                Align = alTop
                Right = 0
                PreferDownStyles = True
                Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
                LinkPage = faq
                LinkAnchor = Q2
              end
            end
          end
          object faq: TdhPage
            Left = 8
            Top = 16
            Height = 1544
            AutoSizeXY = asNone
            Anchors = [akLeft, akTop, akRight]
            Right = 8
            UseIFrame = False
            object Q1: TdhLabel
              Left = 24
              Top = 48
              Width = 1000
              Height = 146
              Text = 
                '<big><big>Q1</big></big><br/> '#10'<br/><b>I get new error messages ' +
                'which was not there before:'#10'<br/>'#10'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
                '&nbsp;&nbsp;&nbsp;&nbsp;<Label5><code>The type %x% cannot be res' +
                'olved. It is indirectly referenced from required .class files</c' +
                'ode></Label5>'#10'<br/></b>'#10'<br/>'#10'The PingPong<sup></sup> compiler r' +
                'equires to evaluate annotations from super classes of a referenc' +
                'ed class. For this reason, the superclass must be in the build p' +
                'ath of the project. To achieve this, you have to add a dependenc' +
                'y to the project or plugin which contains this class file.'
              AutoSizeXY = asY
            end
            object Q2: TdhLabel
              Left = 24
              Top = 224
              Width = 1000
              Height = 164
              Text = 
                '<big><big>Q2</big></big><br/> '#10'<br/><b>I get the error message :' +
                #10'<br/>'#10'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<La' +
                'bel5><code>The import pingpong cannot be resolved</code></Label5' +
                '>'#10'<br/></b>'#10'<br/>'#10'You try to import one of the three annotations' +
                ' CanBeNull, NonNull or NonNullByDefault, but they are unknown to' +
                ' the Java compiler. As normal Java classes, annotations must be ' +
                'on the build path. '#10'For this reason, download <Link7>pingpong.an' +
                'notations.jar</Link7>, copy it to your Java project e.g. in its ' +
                'root folder and choose "Add to build path" from the context menu' +
                '. '
              AutoSizeXY = asY
            end
          end
          object features: TdhPage
            Left = 8
            Top = 16
            Height = 688
            AutoSizeXY = asNone
            Anchors = [akLeft, akTop, akRight]
            Right = 8
            UseIFrame = False
            object Label7: TdhLabel
              Left = 16
              Top = 24
              Width = 496
              Height = 19
              Text = 'Kinds of reported problems:'
              Style.FontWeight = cfwBold
              AutoSizeXY = asY
            end
            object Label20: TdhLabel
              Left = 72
              Top = 80
              Width = 480
              Height = 18
              Text = 'Objects which can be null cannot be accessed directly.'
              AutoSizeXY = asY
            end
            object Image3: TdhLink
              Left = 16
              Top = 112
              Width = 505
              Height = 51
              ImageType = bitImage
              Style.BackgroundImage.Path = 'images\rule1.GIF'
              Style.BackgroundImage.State = isAnalyzed
              Style.BackgroundImage.Width = 505
              Style.BackgroundImage.Height = 51
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
            end
            object Label21: TdhLabel
              Left = 72
              Top = 280
              Width = 496
              Height = 72
              Text = 
                'NonNull-declared variables cannot be assigned a value which can ' +
                'be null. Likewise, NonNull parameters cannot be passed a value w' +
                'hich may be null and return statements of NonNull-declared metho' +
                'ds are not allowed to return values which may be null.'
              AutoSizeXY = asY
            end
            object Link14: TdhLink
              Left = 16
              Top = 376
              Width = 243
              Height = 33
              ImageType = bitImage
              Style.BackgroundImage.Path = 'images\pic7.GIF'
              Style.BackgroundImage.State = isAnalyzed
              Style.BackgroundImage.Width = 243
              Style.BackgroundImage.Height = 33
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
            end
            object Label22: TdhLabel
              Left = 32
              Top = 176
              Width = 648
              Height = 68
              Text = 
                'NOTE: It is assumed, that non-annotated methods/parameters/class' +
                ' fields return a value not equal to null. Thus "old code" is tru' +
                'sted to be not responsibile for null pointers. You can migrate o' +
                'ld code by using PingPong annotations, thus control all null poi' +
                'nters generated by old code.'
              Style.Border.Color = 14590050
              Style.Border.Style = cbsSolid
              Style.Padding = 9
              Style.FontSize = '14'
              Style.Margin = '0'
              Style.TextAlign = ctaJustify
              AutoSizeXY = asY
            end
            object Label23: TdhLabel
              Left = 8
              Top = 80
              Width = 48
              Height = 18
              Text = 'Rule 1:'
              Use = rule
              AutoSizeXY = asXY
            end
            object Label24: TdhLabel
              Left = 8
              Top = 280
              Width = 48
              Height = 18
              Text = 'Rule 2:'
              Use = rule
              AutoSizeXY = asXY
            end
            object Label19: TdhLabel
              Left = 8
              Top = 432
              Width = 48
              Height = 18
              Text = 'Rule 3:'
              Use = rule
              AutoSizeXY = asXY
            end
            object Label30: TdhLabel
              Left = 80
              Top = 432
              Width = 480
              Height = 72
              Text = 
                'Even when not using PingPong annotations, nullibility problems w' +
                'ithin your code may be reported. In the following example, a NPE' +
                ' would be thrown if the while-body would be executed a second ti' +
                'me, so an error is reported.'
              AutoSizeXY = asY
            end
            object Image4: TdhLink
              Left = 16
              Top = 520
              Width = 275
              Height = 86
              ImageType = bitImage
              Style.BackgroundImage.Path = 'images\pic8.GIF'
              Style.BackgroundImage.State = isAnalyzed
              Style.BackgroundImage.Width = 275
              Style.BackgroundImage.Height = 86
              AutoSizeXY = asXY
              PreferDownStyles = True
              Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
            end
          end
          object contact: TdhPage
            Left = 8
            Top = 16
            Height = 600
            AutoSizeXY = asNone
            Anchors = [akLeft, akTop, akRight]
            Right = 24
            UseIFrame = False
            object PageControl2: TdhPageControl
              Left = 63
              Top = 69
              Width = 24
              Height = 24
              ActivePage = Page7
              FixedHeight = True
              object Page6: TdhPage
                Left = 15
                Top = 13
                Width = 216
                Height = 256
                AutoSizeXY = asNone
                UseIFrame = False
                object ContactForm: TdhHTMLForm
                  Left = 8
                  Top = 8
                  Width = 200
                  Height = 240
                  AutoSizeXY = asNone
                  Method = fmtPost
                  object Submit1: TdhLink
                    Left = 8
                    Top = 208
                    Width = 63
                    Height = 24
                    Text = 'Submit'
                    AutoSizeXY = asXY
                    Layout = ltButton
                    PreferDownStyles = True
                    Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
                    LinkPage = Page7
                    FormButtonType = fbSubmit
                  end
                  object Label25: TdhLabel
                    Left = 4
                    Top = 6
                    Width = 81
                    Height = 18
                    Text = 'Your Name:'
                    AutoSizeXY = asXY
                  end
                  object name: TdhEdit
                    Left = 8
                    Top = 24
                    Width = 184
                    Height = 23
                    AutoSizeXY = asY
                  end
                  object Label26: TdhLabel
                    Left = 8
                    Top = 46
                    Width = 49
                    Height = 18
                    Text = 'E-Mail:'
                    AutoSizeXY = asXY
                  end
                  object email: TdhEdit
                    Left = 8
                    Top = 64
                    Width = 184
                    Height = 23
                    AutoSizeXY = asY
                  end
                  object Label27: TdhLabel
                    Left = 8
                    Top = 86
                    Width = 69
                    Height = 18
                    Text = 'Message:'
                    AutoSizeXY = asXY
                  end
                  object message: TdhMemo
                    Left = 8
                    Top = 104
                    Width = 184
                    Height = 96
                    AutoSizeXY = asNone
                  end
                  object Reset1: TdhLink
                    Left = 80
                    Top = 208
                    Width = 58
                    Height = 24
                    Text = 'Reset'
                    AutoSizeXY = asXY
                    Layout = ltButton
                    PreferDownStyles = True
                    Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
                    FormButtonType = fbReset
                  end
                  object access: TdhHiddenField
                    Left = 144
                    Top = 208
                    Width = 24
                    Height = 24
                    Value = 'dfm2html'
                  end
                end
              end
              object Page7: TdhPage
                Left = 15
                Top = 13
                Width = 216
                Height = 256
                Style.PaddingTop = 30
                AutoSizeXY = asNone
                UseIFrame = False
                object ProcessContactForm: TdhDirectHTML
                  Left = 0
                  Top = 30
                  Height = 19
                  AutoSizeXY = asXY
                  Align = alTop
                  Right = 0
                  InnerHTML = 
                    '<?php'#13#10#9'$recipient = "pingpong@kiegeland.com";'#13#10#9'$subject = "Con' +
                    'tact form";'#13#10#13#10#9'if ($_POST["access"]!="dfm2html")  $err="Page in' +
                    'correctly accessed (e.g. by a bot)"; else'#13#10#9'if ($_POST["name"] =' +
                    '= "")  $err="You must specify a name!"; else'#13#10#9'if ($_POST["messa' +
                    'ge"] == "")  $err="You must specify a message!"; else'#13#10#9'if ($rec' +
                    'ipient == "your@email.com") $err="No recipient defined by the we' +
                    'b designer!"; else'#13#10#9'$err="";'#13#10#9'if ($err=="") {'#13#10#9#9'$msg= "";'#13#10#9#9 +
                    'foreach($_POST as $key=> $val) {'#13#10#9#9#9'if ($key != "access") $msg ' +
                    '.= $key." : ".$val."\n\n";'#13#10#9#9'}'#13#10#9#9'$header='#39#39';'#13#10#9#9'if (isset($_PO' +
                    'ST["email"])) $header .= '#39'From:'#39'.$_POST['#39'email'#39']."\n";'#13#10#13#10#9#9'if (' +
                    'count($_FILES)>0) {'#13#10#13#10#9#9'$boundary = strtoupper(md5(uniqid(time(' +
                    '))));'#13#10#13#10#9#9'$header .= "MIME-Version: 1.0\n";'#13#10#9#9'$header .= "Cont' +
                    'ent-Type: multipart/mixed; boundary=$boundary\n\n";'#13#10#9#9'$header .' +
                    '= "This is a multi-part message in MIME format.\n\n";'#13#10#9#9'$header' +
                    ' .= "--$boundary\n";'#13#10#9#9'$header .= "Content-Type: text/plain\n";' +
                    #13#10#9#9'$header .= "Content-Transfer-Encoding: 8bit\n\n";'#13#10#9#9'$header' +
                    ' .= "$msg\n";'#13#10#9#9'$msg='#39#39';'#13#10#13#10#9#9#13#10#9#9'foreach ($_FILES as $filefiel' +
                    'd => $file) if (is_uploaded_file($file['#39'tmp_name'#39'])) {'#13#10#9#9#9#13#10#9#9#9 +
                    '$content=chunk_split(base64_encode(fread(fopen($file['#39'tmp_name'#39']' +
                    ',"r"),filesize($file['#39'tmp_name'#39']))));'#13#10#13#10#9#9#9'$header .= "--$bound' +
                    'ary\n";'#13#10#9#9#9'$header .= "Content-Type: ".$file['#39'type'#39']."; name=\"' +
                    '".$file['#39'name'#39']."\"\n";'#13#10#9#9#9'$header .= "Content-Transfer-Encodin' +
                    'g: base64\n";'#13#10#9#9#9'$header .= "Content-Disposition: attachment; f' +
                    'ilename=\"".$file['#39'name'#39']."\"\n\n";'#13#10#9#9#9'$header .= "$content\n";' +
                    #13#10#13#10#9#9'}'#13#10#9#9'$header .= "--$boundary--";'#13#10#9#9'}'#13#10#9#9'mail($recipient, ' +
                    '$subject, $msg, $header);'#13#10#13#10'?>'
                  GenerateContainer = False
                end
                object Label28: TdhLabel
                  Left = 0
                  Top = 49
                  Height = 36
                  Text = 'Thank you for your message, <?php echo $_POST["name"]?>!'
                  Style.TextAlign = ctaCenter
                  Style.FontStyle = cfsItalic
                  Style.FontWeight = cfwBold
                  AutoSizeXY = asXY
                  Align = alTop
                  Right = 0
                end
                object DirectHTML2: TdhDirectHTML
                  Left = 0
                  Top = 85
                  Height = 19
                  AutoSizeXY = asXY
                  Align = alTop
                  Right = 0
                  InnerHTML = '<?php'#13#10#9'} else {'#13#10'?>'
                  GenerateContainer = False
                end
                object Label29: TdhLabel
                  Left = 0
                  Top = 104
                  Height = 24
                  Text = '<?php echo $err?> &nbsp;&nbsp;<Link23>Go back</Link23>'
                  Style.TextAlign = ctaCenter
                  Style.Color = Red
                  AutoSizeXY = asXY
                  Align = alTop
                  Right = 0
                end
                object DirectHTML3: TdhDirectHTML
                  Left = 0
                  Top = 128
                  Height = 19
                  AutoSizeXY = asXY
                  Align = alTop
                  Right = 0
                  InnerHTML = '<?php'#13#10#9'}'#13#10'?>'
                  GenerateContainer = False
                end
                object StyleSheet3: TdhStyleSheet
                  Left = 16
                  Top = 152
                  Width = 28
                  Height = 28
                  Expanded = False
                  ExpandedWidth = 100
                  ExpandedHeight = 100
                  Expanded = False
                  object Link23: TdhLink
                    Left = 0
                    Top = 0
                    Height = 24
                    Text = 'Link1'
                    AutoSizeXY = asXY
                    Align = alTop
                    Right = 0
                    Layout = ltButton
                    PreferDownStyles = True
                    Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
                    Link = 'javascript:history.back()'
                  end
                end
              end
            end
            object Text3: TdhLabel
              Left = 16
              Width = 216
              Height = 18
              Text = 'PingPong &copy; J. Kiegeland 2010'
              Style.Effects.Enabled = True
              Style.Effects.AntiAliasing = True
              Style.Effects.Text = etInclude
              AutoSizeXY = asXY
              Anchors = [akLeft, akBottom]
              Bottom = 299
            end
          end
        end
      end
      object StyleSheet2: TdhStyleSheet
        Left = 40
        Top = 344
        Width = 28
        Height = 28
        Expanded = False
        ExpandedWidth = 100
        ExpandedHeight = 100
        Expanded = False
        object Label8: TdhLabel
          Left = 0
          Top = 0
          Height = 18
          Text = 'Label8'
          Style.Color = -1224736768
          AutoSizeXY = asY
          Align = alTop
          Right = 0
        end
        object Label11: TdhLabel
          Left = 0
          Top = 18
          Height = 18
          Text = 'Label11'
          Style.Color = Red
          AutoSizeXY = asY
          Align = alTop
          Right = 0
        end
        object rule: TdhLabel
          Left = 0
          Top = 36
          Height = 18
          Text = 'rule'
          Style.TextDecoration = [ctdUnderline]
          AutoSizeXY = asY
          Align = alTop
          Right = 0
        end
      end
      object Link12: TdhLink
        Left = 32
        Top = 280
        Width = 54
        Height = 18
        Text = 'Contact'
        Use = Link4
        AutoSizeXY = asXY
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
        LinkPage = contact
      end
      object Link13: TdhLink
        Left = 32
        Top = 160
        Width = 62
        Height = 18
        Text = 'Features'
        Use = Link4
        AutoSizeXY = asXY
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
        LinkPage = features
      end
    end
    object StyleSheet1: TdhStyleSheet
      Left = 840
      Top = 328
      Width = 28
      Height = 28
      Expanded = False
      ExpandedWidth = 100
      ExpandedHeight = 100
      Expanded = False
      object Label3: TdhLabel
        Left = 0
        Top = 0
        Height = 18
        Text = 'Label3'
        AutoSizeXY = asY
        Align = alTop
        Right = 0
      end
      object Label5: TdhLabel
        Left = 0
        Top = 18
        Height = 18
        Text = 'Label5'
        Style.Color = Red
        AutoSizeXY = asY
        Align = alTop
        Right = 0
      end
      object Link4: TdhLink
        Left = 0
        Top = 36
        Height = 18
        Text = 'Link4'
        Style.Color = Black
        AutoSizeXY = asXY
        Align = alTop
        Right = 0
        StyleDown.Color = Blue
        StyleOver.Color = Blue
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
        Link = 'http://'
      end
      object Link7: TdhLink
        Left = 0
        Top = 54
        Height = 18
        Text = 'annotations'
        AutoSizeXY = asXY
        Align = alTop
        Right = 0
        PreferDownStyles = True
        Options = [loDownIfMenu, loDownIfMouseDown, loDownIfURL]
        Link = './pingpong.annotations.jar'
      end
    end
  end
end
